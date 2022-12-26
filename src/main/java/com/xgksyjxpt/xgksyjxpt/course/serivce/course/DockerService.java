package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.Container;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import com.xgksyjxpt.xgksyjxpt.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DockerService {

    @Autowired
    private ContainerService containerService;

    private static DockerClient dockerClient= DockerUtil.queryDockerClient(DockerConfig.DOCKER_API_URL);

    /**
     * 查询容器ip
     * @param id 容器id
     * @param networkName 网卡名称
     * @return
     */
    public String getIp(String id,String networkName){
        String ip=DockerUtil.getContainersIp(dockerClient,id,networkName);
        return ip;
    }


    /**
     * 根据镜像名和学生id创建容器并并返回id
     */
    public String createQueryId(String imagesName,String stuId,String networkName,String testid) throws Exception {

        String id=DockerUtil.getContainersId(dockerClient,imagesName+"-"+stuId);
//        先判断本地是不是已经存在该名称的容器,不存在才创建
        if(id==null){
            HostConfig hostConfig=new HostConfig().withNetworkMode(networkName).withPrivileged(true);
            List<Image> imageList=DockerUtil.imageList(dockerClient);
            //存放本地镜像名
            String localName=null;

            int count=0;
            //生成随机8位密码
            String sshpasswd=PasswordUtils.getLowerLetterNumber(8);
            //对密码进行加密
            String bash64pa= Base64Converter.encode(sshpasswd);
            for (Image im :imageList) {
                //不处理none镜像
                if(!im.getRepoTags()[0].split(":")[0].equals("<none>")){
                    localName=im.getRepoTags()[0].split(":")[0];
                    //判断本地是否有该镜像
                    if (localName.equals(imagesName)){
                        //运行容器
                        id=DockerUtil.runContainers(dockerClient,im.getRepoTags()[0],imagesName+"-"+stuId,hostConfig,sshpasswd);
                        break;
                    }
                    count++;
                }
            }
            //保存到数据库中
            Container container= Container.builder()
                    .container_id(id)
                    .container_startTime(new Date())
                    .passwd(bash64pa)
                    .test_id(testid)//实验id
                    .stu_id(stuId)
                    .build();
            try {
                //添加记录
                int stu=containerService.createContainer(container);
                //添加记录是否成功
                if (stu==0){
                    //插入记录失败删除容器
                    DockerUtil.removeContainer(dockerClient,id);
                    id=null;
                }
            }catch (Exception e){
//                发生异常删除容器
                DockerUtil.removeContainer(dockerClient,id);
                id=null;
                e.printStackTrace();
            }
        }
        return id;
    }
    /**
     * 批量移除容器
     */
    public int removeContainers(List<String> cids){
        int stu=0;

        for (String s:cids
             ) {
            DockerUtil.removeContainer(dockerClient,s);
            stu++;
        }
        return stu;
    }
    /**
     * 查询容器名称
     */
    public String selectContainersName(String cid){
        return DockerUtil.getContainersName(dockerClient,cid);
    }

}
