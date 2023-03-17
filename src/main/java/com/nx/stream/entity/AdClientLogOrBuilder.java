// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ad_client_log.proto

package com.nx.stream.entity;

public interface AdClientLogOrBuilder extends
    // @@protoc_insertion_point(interface_extends:grpc.AdClientLog)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string request_id = 1;</code>
   */
  java.lang.String getRequestId();
  /**
  /**
   * <code>optional string request_id = 1;</code>
   */
  com.google.protobuf.ByteString
      getRequestIdBytes();

  /**
   * <code>optional uint64 timestamp = 2;</code>
   */
  long getTimestamp();

  /**
   * <code>optional string device_id = 11;</code>
   */
  java.lang.String getDeviceId();
  /**
   * <code>optional string device_id = 11;</code>
   */
  com.google.protobuf.ByteString
      getDeviceIdBytes();

  /**
   * <code>optional string os = 12;</code>
   */
  java.lang.String getOs();
  /**
   * <code>optional string os = 12;</code>
   */
  com.google.protobuf.ByteString
      getOsBytes();

  /**
   * <code>optional string network = 13;</code>
   */
  java.lang.String getNetwork();
  /**
   * <code>optional string network = 13;</code>
   */
  com.google.protobuf.ByteString
      getNetworkBytes();

  /**
   * <pre>
   * dmp info
   * </pre>
   *
   * <code>optional string user_id = 51;</code>
   */
  java.lang.String getUserId();
  /**
   * <pre>
   * dmp info
   * </pre>
   *
   * <code>optional string user_id = 51;</code>
   */
  com.google.protobuf.ByteString
      getUserIdBytes();

  /**
   * <pre>
   * ad info
   * </pre>
   *
   * <code>optional string source_type = 100;</code>
   */
  java.lang.String getSourceType();
  /**
   * <pre>
   * ad info
   * </pre>
   *
   * <code>optional string source_type = 100;</code>
   */
  com.google.protobuf.ByteString
      getSourceTypeBytes();

  /**
   * <code>optional string bid_type = 101;</code>
   */
  java.lang.String getBidType();
  /**
   * <code>optional string bid_type = 101;</code>
   */
  com.google.protobuf.ByteString
      getBidTypeBytes();

  /**
   * <code>optional int64 pos_id = 150;</code>
   */
  long getPosId();

  /**
   * <code>optional int64 account_id = 151;</code>
   */
  long getAccountId();

  /**
   * <code>optional int64 unit_id = 152;</code>
   */
  long getUnitId();

  /**
   * <code>optional int64 creative_id = 153;</code>
   */
  long getCreativeId();

  /**
   * <pre>
   * event
   * </pre>
   *
   * <code>optional string event_type = 211;</code>
   */
  java.lang.String getEventType();
  /**
   * <pre>
   * event
   * </pre>
   *
   * <code>optional string event_type = 211;</code>
   */
  com.google.protobuf.ByteString
      getEventTypeBytes();
}
