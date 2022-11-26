package com.xgksyjxpt.xgksyjxpt.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class DockerUtil {

    /**
     * 根据url返回dockerclient
     * @param url
     * @return
     */
    public static DockerClient queryDockerClient(String url){
//                设置docker配置
        DockerClientConfig custom = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(url)
                .withDockerTlsVerify(false)
                .build();
//        根据配置获取客户端
        DockerClient dockerClient = DockerClientBuilder.getInstance(custom).build();


        return dockerClient;
    }

    /**
     * 创建并启动容器
     * 注意：该配置针对alpine镜像,如需使用其他镜像，请自行修改配置
     * @param dockerClient
     * @param imagesName
     * @param containersName
     * @return
     */
    public static String runContainers(DockerClient dockerClient,String imagesName,String containersName,HostConfig hostConfig,String sshpasswd){
//        创建容器
        CreateContainerResponse container1 = dockerClient.createContainerCmd(imagesName) //创建容器
                .withName(containersName) //设置容器名
                .withHostConfig(hostConfig) //应用容器配置
                .withStdinOpen(true) ////开启标准输入
                .exec();//执行
//        运行容器
        dockerClient.startContainerCmd(container1.getId()).exec();
//        启动时运行ssh
        //修改密码
        execC(dockerClient,container1.getId(),"/usr/sbin/sshd");
        //启动时设置root密码
        execC(dockerClient,container1.getId(),"/bin/sh","-c","echo root:"+sshpasswd+"|chpasswd");
        return container1.getId();
    }
    /**
     * 根据容器名称获取容器id
     * @param dockerClient
     * @param containersName
     * @return
     */
    public static String getContainersId(DockerClient dockerClient,String containersName){
        String containersId=null;
        //获取指定名字的容器
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        //把指定容器名字放进数组里并让过滤器去查找name这个字段
        listContainersCmd.getFilters().put("name", Arrays.asList(containersName));
        //执行命令
        List<Container> containers = listContainersCmd.exec();
        containersId=containers.get(0).getId();
        return containersId;
    }
    /**
     * 根据id查询容器的ip
     */
    public static String getContainersIp(DockerClient dockerClient,String containersId,String networkName){
        //获取容器详细信息
        InspectContainerResponse info =dockerClient.inspectContainerCmd(containersId).exec();
//        获取容器网络设置
        NetworkSettings networkSettings=info.getNetworkSettings();
//        获取容器网络信息
       Map<String, ContainerNetwork> map=networkSettings.getNetworks();
       String containersIp=null;
       //拿到容器该网卡的网络设置
       ContainerNetwork containerNetwork=map.get(networkName);
       ContainerNetwork.Ipam ipam=containerNetwork.getIpamConfig();
        if (ipam==null){
           containersIp=containerNetwork.getIpAddress();
        }else{
            //赋值
            containersIp=ipam.getIpv4Address();
        }
        return containersIp;
    }

    /**
     * 获取镜像列表
     *
     * @param client
     * @return
     */
    public static List<Image> imageList(DockerClient client) {
        List<Image> imageList = client.listImagesCmd().withShowAll(true).exec();
        return imageList;
    }

    /**
     * 在容器内执行命令
     */
    public static void execC(DockerClient client,String id,String... cmd){
        // 创建命令
        ExecCreateCmdResponse execCreateCmdResponse = client.execCreateCmd(id)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(cmd)
                .withPrivileged(true)
                .withUser("root")
                .exec();
        client.execStartCmd(execCreateCmdResponse.getId()).exec(new ExecStartResultCallback(System.out, System.err));
    }

}
