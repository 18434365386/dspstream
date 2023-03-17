package com.nx.stream.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.nx.stream.entity.AdClientLog;
import com.nx.stream.entity.AdLog;
import com.nx.stream.entity.AdServerLog;
import com.nx.stream.entity.ProcessInfo;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import redis.clients.jedis.Jedis;
import org.apache.hadoop.hbase.client.Put;

import java.io.IOException;

import java.io.IOException;

public class ETLUtils {

    static final String RETRY_TOPIC = Constants.CLIENT_LOG_RETRY;
    public static void sendRetry(Producer producer, byte[] value) {
        sendKafka(producer, RETRY_TOPIC, value);
    }

    public static void sendKafka(Producer producer, String topic, byte[] value) {
        ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, value);
        producer.send(record);
    }
    public static byte[] generateBytesKey(AdClientLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }

    public static byte[] generateBytesKey(AdServerLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }

    public static byte[] generateBytesKey(AdLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }

    public static AdServerLog generateContext(AdServerLog adServerLog) {
        AdServerLog.Builder context = AdServerLog.newBuilder();
        context.setGender(adServerLog.getGender());
        context.setAge(adServerLog.getAge());
        context.setCountry(adServerLog.getCountry());
        context.setSourceType(adServerLog.getSourceType());
        context.setBidType(adServerLog.getBidType());
        return context.build();
    }
//    写入Redis
    static final int DEFAULT_EXPIRE = 1 * 60 * 60;
    public static void writeRedis(Jedis jedis, byte[] key, AdServerLog context) {
        jedis.set(key, context.toByteArray());
        jedis.expire(key, DEFAULT_EXPIRE);
    }
// 写入Hbase
    static final byte[] DEFAULT_COLUMN_FAMILY = Bytes.toBytes("cf1");
    static final byte[] DEFAULT_COLUMN_KEY = Bytes.toBytes("v");

    public static void writeHbase(HTable hTable, byte[] key, AdServerLog context) {
        try {
            Put put = new Put(key);
            put.addColumn(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_KEY, context.toByteArray());
            hTable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static AdServerLog getContext(Jedis jedis, HTable hTable, byte[] key) {
        AdServerLog context = getContext(jedis, key);
        if (context == null)
            context = getContext(hTable, key);
        return context;
    }

    public static AdServerLog getContext(Jedis jedis, byte[] key) {
        byte[] data = jedis.get(key);
        if (data == null)
            return null;
        try {
            return AdServerLog.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AdServerLog getContext(HTable hTable, byte[] key) {
        Get get = new Get(key);
        try {
            Result data = hTable.get(get);
            if (data == null)
                return null;
            return AdServerLog.parseFrom(data.getValue(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static AdLog buildAdLog(AdClientLog adClientLog, AdServerLog context) {
        AdLog.Builder adLogBuilder = AdLog.newBuilder();
        ProcessInfo.Builder processInfoBuilder = ProcessInfo.newBuilder();
        processInfoBuilder.setProcessTimestamp(System.currentTimeMillis());

        adLogBuilder.setRequestId(adClientLog.getRequestId());
        adLogBuilder.setTimestamp(adClientLog.getTimestamp());
        adLogBuilder.setDeviceId(adClientLog.getDeviceId());
        adLogBuilder.setOs(adClientLog.getOs());
        adLogBuilder.setNetwork(adClientLog.getNetwork());
        adLogBuilder.setUserId(adClientLog.getUserId());
        if (context != null) {
            processInfoBuilder.setJoinServerLog(true);
            joinContext(adLogBuilder, context);
        } else {
            processInfoBuilder.setRetryCount(1);
            processInfoBuilder.setJoinServerLog(false);
        }
        adLogBuilder.setSourceType(adClientLog.getSourceType());
        adLogBuilder.setPosId(adClientLog.getPosId());
        adLogBuilder.setAccountId(adClientLog.getAccountId());
        adLogBuilder.setCreativeId(adClientLog.getCreativeId());
        adLogBuilder.setUnitId(adClientLog.getUnitId());
        switch (adClientLog.getEventType()) {
            case "SEND":
                adLogBuilder.setSend(1);
                break;
            case "IMPRESSION":
                adLogBuilder.setImpression(1);
                break;
            case "CLICK":
                adLogBuilder.setClick(1);
                break;
            case "DOWNLOAD":
                adLogBuilder.setDownload(1);
                break;
            case "INSTALLED":
                adLogBuilder.setInstalled(1);
                break;
            case "PAY":
                adLogBuilder.setPay(1);
                break;
            default:
                break;
        }
        return adLogBuilder.build();
    }
    public static void joinContext(AdLog.Builder adLogBuilder, AdServerLog context) {
        adLogBuilder.setGender(context.getGender());
        adLogBuilder.setAge(context.getAge());
        adLogBuilder.setCountry(context.getCountry());
        adLogBuilder.setProvince(context.getProvince());
        adLogBuilder.setCity(context.getCity());

        adLogBuilder.setBidPrice(context.getBidPrice());
    }

}
