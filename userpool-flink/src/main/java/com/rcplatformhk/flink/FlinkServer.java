package com.rcplatformhk.flink;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.collect.Maps;

import com.google.common.collect.Sets;
import com.mysql.jdbc.StringUtils;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.utils.Map2ObjectUtil;
import com.rcplatformhk.utils.SerializeUtils;
import com.twitter.chill.protobuf.ProtobufSerializer;
import net.wicp.tams.common.Conf;
import net.wicp.tams.common.apiext.IOUtil;
import net.wicp.tams.common.flink.source.binlog.BinlogSource;
import net.wicp.tams.duckula.client.Protobuf3;
import org.apache.flink.api.common.functions.FilterFunction;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.netty4.io.netty.util.internal.StringUtil;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;

import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisClusterConfig;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;


public class FlinkServer {

    private static final String CQ = "CACHE_QUEUE";
    private static final Logger logger = LoggerFactory.getLogger(FlinkServer.class);

    public static final class RedisExampleMapper implements RedisMapper<UserInfo> {
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.RPUSH);
        }

        public String getKeyFromData(UserInfo data) {
            return CQ;
        }

        public String getValueFromData(UserInfo data) {
            return SerializeUtils.serialize(data, PropertyNamingStrategy.LOWER_CAMEL_CASE).get();
        }
    }

    public static void main(String[] args) {
        try {
            final ParameterTool params = ParameterTool.fromArgs(args);
            // set up the execution environment
            final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
            env.getConfig().registerTypeWithKryoSerializer(Protobuf3.DuckulaEvent.class, ProtobufSerializer.class);
            env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
            env.enableCheckpointing(5000); // create a checkpoint every 5 seconds
            env.getConfig().setGlobalJobParameters(params); // make parameters available in the web interface
            // make parameters available in the web interface
            env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
            Conf.overProp(IOUtil.fileToProperties("/binlog.properties", FlinkServer.class));
            //监控binlog
            DataStream<Protobuf3.DuckulaEvent> source = env.addSource(new BinlogSource());
            //转换bean对象
            SingleOutputStreamOperator<UserInfo> map = source.filter(
                    (FilterFunction<Protobuf3.DuckulaEvent>) value -> value.getOptType().name().equals("update")
                            || value.getOptType().name().equals("insert"))
                    .map((MapFunction<Protobuf3.DuckulaEvent, UserInfo>)
                            e -> Map2ObjectUtil.mapToObject(Maps.newHashMap(e.getAfterMap()), UserInfo.class))
                    .filter((FilterFunction<UserInfo>) value -> value.getGender() == 1);
            FlinkJedisClusterConfig conf = new FlinkJedisClusterConfig.Builder().setNodes(loadClusters())
                    .build();
            map.addSink(new RedisSink<>(conf, new RedisExampleMapper()));
            env.execute("Flink_binlog_to_redis");
        } catch (Exception e) {
            logger.error("FlinkServer Exception{}", e.getMessage(), e);
        }
    }

    public static Set<InetSocketAddress> loadClusters() {
        Set<InetSocketAddress> inetSocketAddresses;
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/cluster.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("FlinkServer loadClusters Exception{}", e.getMessage(), e);
        }
        String clusters = properties.getProperty("cluster");
        inetSocketAddresses = Arrays.stream(clusters.split(";")).filter(x -> !StringUtil.isNullOrEmpty(x))
                .map(x -> {
                    String[] strs = x.split(":");
                    return InetSocketAddress.createUnresolved(strs[0], Integer.parseInt(strs[1]));
                }).collect(Collectors.toSet());
        return inetSocketAddresses;
    }
}
