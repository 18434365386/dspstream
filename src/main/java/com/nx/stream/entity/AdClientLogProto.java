// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ad_client_log.proto

package com.nx.stream.entity;

public final class AdClientLogProto {
  private AdClientLogProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpc_AdClientLog_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpc_AdClientLog_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023ad_client_log.proto\022\004grpc\"\377\001\n\013AdClient" +
      "Log\022\022\n\nrequest_id\030\001 \001(\t\022\021\n\ttimestamp\030\002 \001" +
      "(\004\022\021\n\tdevice_id\030\013 \001(\t\022\n\n\002os\030\014 \001(\t\022\017\n\007net" +
      "work\030\r \001(\t\022\017\n\007user_id\0303 \001(\t\022\023\n\013source_ty" +
      "pe\030d \001(\t\022\020\n\010bid_type\030e \001(\t\022\017\n\006pos_id\030\226\001 " +
      "\001(\003\022\023\n\naccount_id\030\227\001 \001(\003\022\020\n\007unit_id\030\230\001 \001" +
      "(\003\022\024\n\013creative_id\030\231\001 \001(\003\022\023\n\nevent_type\030\323" +
      "\001 \001(\tB*\n\024com.nx.stream.entityB\020AdClientL" +
      "ogProtoP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_grpc_AdClientLog_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_grpc_AdClientLog_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpc_AdClientLog_descriptor,
        new java.lang.String[] { "RequestId", "Timestamp", "DeviceId", "Os", "Network", "UserId", "SourceType", "BidType", "PosId", "AccountId", "UnitId", "CreativeId", "EventType", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
