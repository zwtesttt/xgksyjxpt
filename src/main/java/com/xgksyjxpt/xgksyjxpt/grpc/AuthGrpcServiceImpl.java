package com.xgksyjxpt.xgksyjxpt.grpc;

import com.xgksyjxpt.xgksyjxpt.login.domain.JwtUitls;
import com.xgksyjxpt.xgksyjxpt.proto.Request;
import com.xgksyjxpt.xgksyjxpt.proto.Response;
import com.xgksyjxpt.xgksyjxpt.proto.VerifyAuthGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class AuthGrpcServiceImpl extends VerifyAuthGrpc.VerifyAuthImplBase {
    @Autowired
    private JwtUitls jwtUitls;

    /**
     * 认证权限服务
     * @param request
     * @param responseObserver
     */
    @Override
    public void authToken(Request request, StreamObserver<Response> responseObserver) {
        String token=request.getJwtText();
        System.out.println(token);
        //接收到请求，开始验证
        Response response= Response.newBuilder().setResult(jwtUitls.verify(token)).build();
        System.out.println(response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
