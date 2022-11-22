package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.github.dockerjava.api.DockerClient;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import org.springframework.stereotype.Service;

@Service
public class DockerService {

    /**
     * 查询容器ip
     * @param id 容器id
     * @param url dockerapi地址
     * @param networkName 网卡名称
     * @return
     */
    public String getIp(String id,String url,String networkName){
        DockerClient dockerClient= DockerUtil.createCon(url);
        String ip=DockerUtil.getContainersIp(dockerClient,id,networkName);
        return ip;
    }


}
