package com.nx.stream.ad;

import com.nx.stream.entity.AdClientLog;
import com.nx.stream.entity.AdLog;
import com.nx.stream.entity.AdServerLog;
import com.nx.stream.entity.schema.AdLogSchema;
import com.nx.stream.utils.FlinkKafkaConsumerUtils;
import com.nx.stream.entity.schema.AdClientLogSchema;
import com.nx.stream.entity.schema.AdServerLogSchema;
import com.nx.stream.utils.Constants;
import com.nx.stream.utils.FlinkKafkaProducerUtils;
import com.twitter.chill.protobuf.ProtobufSerializer;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.IOException;
import java.util.Properties;

public class StreamJoinJob {
    /**
     * 创建KafkaSource
     */
    private static String KAFKA_SERVER_LOG = Constants.SERVER_LOG;
    private static String KAFKA_CLIENT_LOG = Constants.CLIENT_LOG;
    private static String KAFKA_AD_LOG = Constants.AD_LOG;
    private static String KAFKA_AD_LOG_REPORT = Constants.AD_LOG_REPORT;
    private static String BROKERS = Constants.BROKERS;
    public static void main(String[] args) throws Exception {
        String groupId = "flink-stream-join";
        // 定义一个配置 import org.apache.flink.configuration.Configuration;包下
        Configuration configuration = new Configuration();

        // 指定本地WEB-UI端口号
        configuration.setInteger(RestOptions.PORT, 8082);

        // 执行环境使用当前配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);
        env.setStateBackend(new RocksDBStateBackend("hdfs://192.168.6.102:8020/test/"));
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        env.setParallelism(8);
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().enableForceAvro();
        /**
         *  1、设置中间数据序列化和反序列化方式
         *
         */
        env.getConfig().registerTypeWithKryoSerializer(AdServerLog.class, ProtobufSerializer.class);
        env.getConfig().registerTypeWithKryoSerializer(AdLog.class,ProtobufSerializer.class);
        // 2、配置kafka
        Properties properties1 = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_SERVER_LOG, groupId);
        Properties properties2 = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_CLIENT_LOG, groupId);
        DataStreamSource<AdServerLog> adServerLogDataStreamSource = env.addSource(new FlinkKafkaConsumer<>(KAFKA_SERVER_LOG, new AdServerLogSchema(), properties1));
        DataStreamSource<AdClientLog> adClientLogDataStreamSource = env.addSource(new FlinkKafkaConsumer<>(KAFKA_SERVER_LOG, new AdClientLogSchema(), properties2));
        adServerLogDataStreamSource.flatMap(new AdServerLogRichFlatMap()).name("WriteServerContext");
        DataStream<AdLog> adLogStream = adClientLogDataStreamSource.flatMap(new AdClientLogRichFlatMap());

        SingleOutputStreamOperator<String> mapData = adLogStream.map(new MapFunction<AdLog, String>() {
            @Override
            public String map(AdLog adLog) throws Exception {
                return adLog.toString();
            }
        });

        Properties producerProperties = FlinkKafkaProducerUtils.getProducerProperties(BROKERS);
        FlinkKafkaProducer<String> adLogSink = new FlinkKafkaProducer<String>(KAFKA_AD_LOG, new SimpleStringSchema(), producerProperties);
        mapData.print();

        mapData.addSink(adLogSink).name("AdLogProcesser");
        env.execute("OrderInfo......");
    }
}
