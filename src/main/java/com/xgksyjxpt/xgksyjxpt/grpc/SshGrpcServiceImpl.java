package com.xgksyjxpt.xgksyjxpt.grpc;

import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.proto.ConServiceGrpc;
import com.xgksyjxpt.xgksyjxpt.proto.SendId;
import com.xgksyjxpt.xgksyjxpt.proto.SshInfo;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class SshGrpcServiceImpl extends ConServiceGrpc.ConServiceImplBase {
    @Autowired
    private DockerService dockerService;

    @Autowired
    private ContainerService containerService;
    /**
     * 查询容器信息
     * @param request
     * @param responseObserver
     */
    @Override
    public void getSshInfo(SendId request, StreamObserver<SshInfo> responseObserver) {
        String id=request.getId();
        try {
            String ip=dockerService.getIp(id, DockerConfig.DOCKER_NETWORK_NAME);
            String passwd=containerService.queryPasswd(id);
            SshInfo sshInfo=SshInfo.newBuilder().setIp(ip).setPassword(passwd).build();
            responseObserver.onNext(sshInfo);
            responseObserver.onCompleted();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
