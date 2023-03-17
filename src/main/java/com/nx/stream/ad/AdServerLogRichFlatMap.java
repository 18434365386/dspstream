package com.nx.stream.ad;

import com.nx.stream.entity.AdServerLog;
import com.nx.stream.utils.Constants;
import com.nx.stream.utils.ETLUtils;
import com.nx.stream.utils.HBaseUtils;
import com.nx.stream.utils.RedisUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Table;
import redis.clients.jedis.Jedis;

public class AdServerLogRichFlatMap extends RichFlatMapFunction<AdServerLog, String> {
    String tableName = Constants.TABLE_NAME;
    HTable hTable;
    Jedis jedis;

    int expire = 1 * 24 * 60 * 60;

    @Override
    public void open(Configuration parameters) throws Exception {
        hTable = (HTable) HBaseUtils.initHbaseClient(tableName);
        jedis = RedisUtils.initRedis();
        super.open(parameters);
    }

    @Override
    public void flatMap(AdServerLog adServerLog, Collector<String> collector) throws Exception {
        byte[] key = ETLUtils.generateBytesKey(adServerLog);
        AdServerLog context = ETLUtils.generateContext(adServerLog);
        ETLUtils.writeRedis(jedis, key, context);
//        ETLUtils.writeHbase(hTable, key, context);
    }
}

