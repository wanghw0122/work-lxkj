package com.rcplatformhk.us.rule;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rcplatformhk.us.service.Behavior;
import com.rcplatformhk.us.service.Checker;
import com.rcplatformhk.us.task.Task;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unchecked")
public class Rule {

    private Map<Checker, Set<Rule>[]> exportTables = Maps.newLinkedHashMap();
    private int delay = 0;
    private RuleType type = RuleType.SYN;
    private Behavior<Task> behavior;
    private boolean save = false;
    private boolean beforeAction = false;
    private Builder builder;
    private String name;

    private Rule(Builder builder) {
        this.exportTables = builder.exportTables;
        this.delay = builder.delay;
        this.type = builder.type;
        this.behavior = builder.behavior;
        this.save = builder.save;
        this.beforeAction = builder.beforeAction;
        this.builder = builder;
        this.name = builder.name;
    }

    public void gc() {
        if (Objects.nonNull(this.getBuilder()))
            this.getBuilder().gc();
        this.exportTables.values().stream()
                .flatMap(sets -> Stream.of(sets[0], sets[1]))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .forEach(rule -> {
                    rule.gc();
                });
    }

    public static Rule root() {
        return Rule.builder().name("root").checker(o -> true).build();
    }

    public static Builder builder() {
        return new Rule.Builder();
    }

    public void flow(Task task) throws Exception {
        if (beforeAction && delay > 0)
            TimeUnit.SECONDS.sleep(delay);
        if (Objects.nonNull(behavior))
            behavior.update(task);
        if (this.save)
            task.sink();
        Set<Rule> rules = checkList(task);
        if (CollectionUtils.isEmpty(rules) && !this.save) {
            log.info("Task {} Discard!! Rule {}", task, this.getName());
        }
        for (Rule rule : rules) {
            rule.flow(task);
        }
    }

    @Override
    public String toString() {
        AtomicInteger i = new AtomicInteger(0);
        return this.name +
                "{" +
                exportTables.values().stream().map(sets -> "check" + i.incrementAndGet() + "(" +
                        sets[0].stream()
                                .map(Rule::toString)
                                .collect(Collectors.joining(",", "[", "]")) +
                        ";" +
                        sets[1].stream()
                                .map(Rule::toString)
                                .collect(Collectors.joining(",", "[", "]")) +
                        ")").collect(Collectors.joining(",")) +
                "}";
    }


    private Set<Rule> checkList(Task task) {
        Set<Rule> sets = Sets.newHashSet();
        if (exportTables == null) return sets;
        try {
            if (type == RuleType.SYN) {
                sets = exportTables.entrySet().stream()
                        .filter(entry -> Objects.nonNull(entry.getKey()))
                        .map(checkerEntry -> {
                            try {
                                return checkerEntry.getKey().check(task) ? checkerEntry.getValue()[0]
                                        : checkerEntry.getValue()[1];
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .filter(collection -> !CollectionUtils.isEmpty(collection))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
            } else {
                for (Checker checker : exportTables.keySet()) {
                    if (checker.check(task)) {
                        sets.addAll(exportTables.get(checker)[0]);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Rule checkList Exception{}", e.getMessage(), e);
        }
        return sets;
    }

    @Getter
    @Setter
    public static final class Builder {
        private Map<Checker, Set<Rule>[]> exportTables = Maps.newLinkedHashMap();
        private int delay = 0;
        private RuleType type = RuleType.SYN;
        private Behavior<Task> behavior = null;
        private boolean save = false;
        private boolean beforeAction = false;
        private Rule rule;
        private String name = "";

        public Builder() {
        }

        public Builder delay(int delay) {
            this.delay = delay;
            if (Objects.isNull(behavior))
                beforeAction = true;
            return this;
        }

        public Builder exportTables(Map<Checker, Set<Rule>[]> exportTables) {
            this.exportTables = exportTables;
            return this;
        }

        public Builder type(RuleType type) {
            this.type = type;
            return this;
        }

        public Builder behavior(Behavior<Task> behavior) {
            this.behavior = behavior;
            return this;
        }

        public Builder save(boolean save) {
            this.save = save;
            return this;
        }

        public Builder save() {
            this.save = true;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder map(Checker checker, Set<Rule> rules0, Set<Rule> rules1) {
            this.exportTables.put(checker, new Set[]{rules0, rules1});
            return this;
        }

        public Builder addRulesThroughChecker(Checker checker, Set<Rule> rules0, Set<Rule> rules1) {
            this.exportTables.get(checker)[0].addAll(rules0);
            this.exportTables.get(checker)[1].addAll(rules1);
            return this;
        }

        public Builder addRuleThroughChecker(Checker checker, Rule rule0, Rule rule1) {
            return addRulesThroughChecker(checker, Sets.newHashSet(rule0), Sets.newHashSet(rule1));
        }

        public Builder addOnly0RuleForAllChecker(Rule rule) {
            return addAllCheckerRule(rule, null);
        }

        public Builder addOnly1RuleForAllChecker(Rule rule) {
            return addAllCheckerRule(null, rule);
        }


        public Builder bindTrueCheckRule(Rule rule) {
            addOnly0RuleForAllChecker(rule);
            return rule.getBuilder();
        }

        public Builder bindCheckRule(Rule rule) {
            bindTrueCheckRule(rule);
            bindFalseCheckRule(rule);
            return rule.getBuilder();
        }

        public Builder bindFalseCheckRule(Rule rule) {
            addOnly1RuleForAllChecker(rule);
            return rule.getBuilder();
        }

        public Builder addAllCheckerRules(Set<Rule> rules0, Set<Rule> rules1) {
            this.exportTables.entrySet().stream().forEach(checkerEntry -> {
                if (!CollectionUtils.isEmpty(rules0))
                    checkerEntry.getValue()[0].addAll(rules0);
                if (!CollectionUtils.isEmpty(rules1))
                    checkerEntry.getValue()[1].addAll(rules1);
            });
            return this;
        }

        public Builder addAllCheckerRule(Rule rule0, Rule rule1) {
            Set<Rule> rules0 = rule0 == null ? null : Sets.newHashSet(rule0);
            Set<Rule> rules1 = rule1 == null ? null : Sets.newHashSet(rule1);
            return addAllCheckerRules(rules0, rules1);
        }

        public Builder beforeAction(boolean beforeAction) {
            this.beforeAction = beforeAction;
            return this;
        }

        public Builder map(Checker checker, Rule... rules) {
            Set<Rule> ruleSet = Sets.newHashSet(rules);
            return map(checker, ruleSet, Sets.newHashSet());
        }

        public Builder checker(Checker checker) {
            return map(checker, Sets.newHashSet(), Sets.newHashSet());
        }

        public Rule build() {
            this.rule = new Rule(this);
            return this.rule;
        }

        public void gc() {
            this.type = null;
            this.behavior = null;
            this.exportTables = null;
            this.rule.builder = null;
            this.rule = null;
            System.gc();
        }

    }
}
