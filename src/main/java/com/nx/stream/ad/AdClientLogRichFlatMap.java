package com.nx.stream.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nx.stream.entity.AdClientLog;
import com.nx.stream.entity.AdLog;
import com.nx.stream.entity.AdServerLog;
import com.nx.stream.utils.*;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Table;
import org.apache.kafka.clients.producer.Producer;
import redis.clients.jedis.Jedis;

public class AdClientLogRichFlatMap extends RichFlatMapFunction<AdClientLog, AdLog> {
    String tableName = Constants.TABLE_NAME;
    HTable hTable;
    Jedis jedis;
    Producer producer;
    ObjectMapper objectMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
//        hTable = (HTable) HBaseUtils.initHbaseClient(tableName);
        jedis = RedisUtils.initRedis();
        producer = KafkaProducerUtils.getProducer();
        objectMapper = new ObjectMapper();
        super.open(parameters);
    }

    @Override
    public void flatMap(AdClientLog adClientLog, Collector<AdLog> collector) throws Exception {
        byte[] key = ETLUtils.generateBytesKey(adClientLog);
        AdServerLog context = ETLUtils.getContext(jedis, key);
        AdLog adLog = ETLUtils.buildAdLog(adClientLog, context);
        if (context == null) {
            ETLUtils.sendRetry(producer, adLog.toByteArray());
        } else {
            collector.collect(adLog);
        }
    }
}


