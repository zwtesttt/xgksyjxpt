package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.domain.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        DockerClient dockerClient= DockerUtil.queryDockerClient(url);
        String ip=DockerUtil.getContainersIp(dockerClient,id,networkName);
        return ip;
    }

    /**
     * 根据镜像名和学生id创建容器并并返回id
     */
    public String createQueryId(String url,String imagesName,String containersName,String networkName){
        DockerClient dockerClient= DockerUtil.queryDockerClient(url);
        HostConfig hostConfig=new HostConfig().withNetworkMode(networkName).withPrivileged(true);
        List<Image> imageList=DockerUtil.imageList(dockerClient);
        String id=null;
        //存放本地镜像名
        String localName=null;
        int count=0;
        for (Image im :imageList) {
            //不处理none镜像
            if(!im.getRepoTags()[0].split(":")[0].equals("<none>")){
                localName=im.getRepoTags()[0].split(":")[0];
                //判断本地是否有该镜像
                if (localName.equals(imagesName)){
                    id=DockerUtil.runContainers(dockerClient,im.getRepoTags()[0],containersName,hostConfig);
                    break;
                }
                count++;
            }
        }
        return id;
    }


}
