package com.xgksyjxpt.xgksyjxpt.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.16.1)",
    comments = "Source: sshService.proto")
public final class ConServiceGrpc {

  private ConServiceGrpc() {}

  public static final String SERVICE_NAME = "proto.ConService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.SendId,
      com.xgksyjxpt.xgksyjxpt.proto.SshInfo> getGetSshInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSshInfo",
      requestType = com.xgksyjxpt.xgksyjxpt.proto.SendId.class,
      responseType = com.xgksyjxpt.xgksyjxpt.proto.SshInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.SendId,
      com.xgksyjxpt.xgksyjxpt.proto.SshInfo> getGetSshInfoMethod() {
    io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.SendId, com.xgksyjxpt.xgksyjxpt.proto.SshInfo> getGetSshInfoMethod;
    if ((getGetSshInfoMethod = ConServiceGrpc.getGetSshInfoMethod) == null) {
      synchronized (ConServiceGrpc.class) {
        if ((getGetSshInfoMethod = ConServiceGrpc.getGetSshInfoMethod) == null) {
          ConServiceGrpc.getGetSshInfoMethod = getGetSshInfoMethod = 
              io.grpc.MethodDescriptor.<com.xgksyjxpt.xgksyjxpt.proto.SendId, com.xgksyjxpt.xgksyjxpt.proto.SshInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "proto.ConService", "GetSshInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xgksyjxpt.xgksyjxpt.proto.SendId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xgksyjxpt.xgksyjxpt.proto.SshInfo.getDefaultInstance()))
                  .setSchemaDescriptor(new ConServiceMethodDescriptorSupplier("GetSshInfo"))
                  .build();
          }
        }
     }
     return getGetSshInfoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ConServiceStub newStub(io.grpc.Channel channel) {
    return new ConServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ConServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ConServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ConServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ConServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ConServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getSshInfo(com.xgksyjxpt.xgksyjxpt.proto.SendId request,
        io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.SshInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getGetSshInfoMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetSshInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.xgksyjxpt.xgksyjxpt.proto.SendId,
                com.xgksyjxpt.xgksyjxpt.proto.SshInfo>(
                  this, METHODID_GET_SSH_INFO)))
          .build();
    }
  }

  /**
   */
  public static final class ConServiceStub extends io.grpc.stub.AbstractStub<ConServiceStub> {
    private ConServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ConServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConServiceStub(channel, callOptions);
    }

    /**
     */
    public void getSshInfo(com.xgksyjxpt.xgksyjxpt.proto.SendId request,
        io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.SshInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetSshInfoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ConServiceBlockingStub extends io.grpc.stub.AbstractStub<ConServiceBlockingStub> {
    private ConServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ConServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.xgksyjxpt.xgksyjxpt.proto.SshInfo getSshInfo(com.xgksyjxpt.xgksyjxpt.proto.SendId request) {
      return blockingUnaryCall(
          getChannel(), getGetSshInfoMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ConServiceFutureStub extends io.grpc.stub.AbstractStub<ConServiceFutureStub> {
    private ConServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ConServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected ConServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ConServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.xgksyjxpt.xgksyjxpt.proto.SshInfo> getSshInfo(
        com.xgksyjxpt.xgksyjxpt.proto.SendId request) {
      return futureUnaryCall(
          getChannel().newCall(getGetSshInfoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SSH_INFO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ConServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ConServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SSH_INFO:
          serviceImpl.getSshInfo((com.xgksyjxpt.xgksyjxpt.proto.SendId) request,
              (io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.SshInfo>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ConServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ConServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.xgksyjxpt.xgksyjxpt.proto.SshService.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ConService");
    }
  }

  private static final class ConServiceFileDescriptorSupplier
      extends ConServiceBaseDescriptorSupplier {
    ConServiceFileDescriptorSupplier() {}
  }

  private static final class ConServiceMethodDescriptorSupplier
      extends ConServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ConServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ConServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ConServiceFileDescriptorSupplier())
              .addMethod(getGetSshInfoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
