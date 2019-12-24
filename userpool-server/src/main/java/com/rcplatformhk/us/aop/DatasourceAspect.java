package com.rcplatformhk.us.aop;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.common.DatabaseType;
import com.rcplatformhk.us.datasource.holder.DatabaseContextHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import sun.security.util.ArrayUtil;

import java.lang.annotation.Annotation;
import java.util.Objects;

@Aspect
@Component
public class DatasourceAspect {

    @Pointcut("execution(* com.rcplatformhk.us.dao.service.*.*(..))")
    public void declareJointPointExpression() {
    }

    @Before("declareJointPointExpression()")
    public void setDataSourceKey(JoinPoint point) {
        Annotation[] annotations = point.getTarget().getClass().getAnnotations();
        DatabaseContextHolder.setDatabaseType(DatabaseType.major);
        if (!ArrayUtils.isEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof DbType) {
                    int id = ((DbType) annotation).type();
                    DatabaseType databaseType = DatabaseType.getTypeById(id);
                    if (Objects.nonNull(databaseType)) {
                        DatabaseContextHolder.setDatabaseType(databaseType);
                        break;
                    }
                }
            }
        }
    }
}
