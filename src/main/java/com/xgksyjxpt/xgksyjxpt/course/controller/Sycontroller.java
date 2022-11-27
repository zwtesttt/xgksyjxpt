package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.course.domain.Container;
import com.xgksyjxpt.xgksyjxpt.course.serivce.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.DockerService;
import com.xgksyjxpt.xgksyjxpt.domain.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;

import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Sycontroller {


    //注入Containerservice对象
    @Autowired
    private ContainerService containerService;


    //注入dockerservice对象
    @Autowired
    private DockerService dockerService;

    /**
     * 根据容器id查询容器ip
     */
    @GetMapping("/getIp")
    @ResponseBody
    public Object getIp(String id){
        ReturnObject re=new ReturnObject();
        String ip=null;
        String url= DockerConfig.DOCKER_API_URL;
        String networkName=DockerConfig.DOCKER_NETWORK_NAME;
        ip=dockerService.getIp(id,url,networkName);
        return ip;
    }
    /**
     * 根据镜像名和学生id创建容器返回容器id
     */
    @PostMapping("/createContain")
    @ResponseBody
    public Object createContain(String imagesName,String stuId){
        ReturnObject re=new ReturnObject();
        try {
            String id= dockerService.createQueryId(DockerConfig.DOCKER_API_URL,imagesName,stuId,DockerConfig.DOCKER_NETWORK_NAME);
            if (id!=null){
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("运行成功");
                re.setData(id);
            }else{
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("运行容器失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("运行容器失败");
        }
        return re;
    }

    /**
     * 查询镜像列表
     */
    @GetMapping("/queryImages")
    @ResponseBody
    public List<String> queryImages(){
        List<String> list=new ArrayList<>();
        //实例化dockerclient对象
        DockerClient dockerClient=DockerUtil.queryDockerClient(DockerConfig.DOCKER_API_URL);
//        查询所有
        List<Image> ll=DockerUtil.imageList(dockerClient);
        //遍历镜像
        for (Image im:ll) {
            //跳过none的镜像,使用split切割字符串
            if(!im.getRepoTags()[0].split(":")[0].equals("<none>")){
                list.add(im.getRepoTags()[0].split(":")[0]);
            }
        }
        return list;
    }
}
