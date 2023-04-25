package com.xgksyjxpt.xgksyjxpt.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 *定义服务
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.16.1)",
    comments = "Source: sshService.proto")
public final class VerifyAuthGrpc {

  private VerifyAuthGrpc() {}

  public static final String SERVICE_NAME = "proto.VerifyAuth";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.Request,
      com.xgksyjxpt.xgksyjxpt.proto.Response> getAuthTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "authToken",
      requestType = com.xgksyjxpt.xgksyjxpt.proto.Request.class,
      responseType = com.xgksyjxpt.xgksyjxpt.proto.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.Request,
      com.xgksyjxpt.xgksyjxpt.proto.Response> getAuthTokenMethod() {
    io.grpc.MethodDescriptor<com.xgksyjxpt.xgksyjxpt.proto.Request, com.xgksyjxpt.xgksyjxpt.proto.Response> getAuthTokenMethod;
    if ((getAuthTokenMethod = VerifyAuthGrpc.getAuthTokenMethod) == null) {
      synchronized (VerifyAuthGrpc.class) {
        if ((getAuthTokenMethod = VerifyAuthGrpc.getAuthTokenMethod) == null) {
          VerifyAuthGrpc.getAuthTokenMethod = getAuthTokenMethod = 
              io.grpc.MethodDescriptor.<com.xgksyjxpt.xgksyjxpt.proto.Request, com.xgksyjxpt.xgksyjxpt.proto.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "proto.VerifyAuth", "authToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xgksyjxpt.xgksyjxpt.proto.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xgksyjxpt.xgksyjxpt.proto.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new VerifyAuthMethodDescriptorSupplier("authToken"))
                  .build();
          }
        }
     }
     return getAuthTokenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static VerifyAuthStub newStub(io.grpc.Channel channel) {
    return new VerifyAuthStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static VerifyAuthBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new VerifyAuthBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static VerifyAuthFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new VerifyAuthFutureStub(channel);
  }

  /**
   * <pre>
   *定义服务
   * </pre>
   */
  public static abstract class VerifyAuthImplBase implements io.grpc.BindableService {

    /**
     */
    public void authToken(com.xgksyjxpt.xgksyjxpt.proto.Request request,
        io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getAuthTokenMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAuthTokenMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.xgksyjxpt.xgksyjxpt.proto.Request,
                com.xgksyjxpt.xgksyjxpt.proto.Response>(
                  this, METHODID_AUTH_TOKEN)))
          .build();
    }
  }

  /**
   * <pre>
   *定义服务
   * </pre>
   */
  public static final class VerifyAuthStub extends io.grpc.stub.AbstractStub<VerifyAuthStub> {
    private VerifyAuthStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VerifyAuthStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VerifyAuthStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VerifyAuthStub(channel, callOptions);
    }

    /**
     */
    public void authToken(com.xgksyjxpt.xgksyjxpt.proto.Request request,
        io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuthTokenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *定义服务
   * </pre>
   */
  public static final class VerifyAuthBlockingStub extends io.grpc.stub.AbstractStub<VerifyAuthBlockingStub> {
    private VerifyAuthBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VerifyAuthBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VerifyAuthBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VerifyAuthBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.xgksyjxpt.xgksyjxpt.proto.Response authToken(com.xgksyjxpt.xgksyjxpt.proto.Request request) {
      return blockingUnaryCall(
          getChannel(), getAuthTokenMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *定义服务
   * </pre>
   */
  public static final class VerifyAuthFutureStub extends io.grpc.stub.AbstractStub<VerifyAuthFutureStub> {
    private VerifyAuthFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VerifyAuthFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected VerifyAuthFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VerifyAuthFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.xgksyjxpt.xgksyjxpt.proto.Response> authToken(
        com.xgksyjxpt.xgksyjxpt.proto.Request request) {
      return futureUnaryCall(
          getChannel().newCall(getAuthTokenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTH_TOKEN = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final VerifyAuthImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(VerifyAuthImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AUTH_TOKEN:
          serviceImpl.authToken((com.xgksyjxpt.xgksyjxpt.proto.Request) request,
              (io.grpc.stub.StreamObserver<com.xgksyjxpt.xgksyjxpt.proto.Response>) responseObserver);
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

  private static abstract class VerifyAuthBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    VerifyAuthBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.xgksyjxpt.xgksyjxpt.proto.SshService.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("VerifyAuth");
    }
  }

  private static final class VerifyAuthFileDescriptorSupplier
      extends VerifyAuthBaseDescriptorSupplier {
    VerifyAuthFileDescriptorSupplier() {}
  }

  private static final class VerifyAuthMethodDescriptorSupplier
      extends VerifyAuthBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    VerifyAuthMethodDescriptorSupplier(String methodName) {
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
      synchronized (VerifyAuthGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new VerifyAuthFileDescriptorSupplier())
              .addMethod(getAuthTokenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
