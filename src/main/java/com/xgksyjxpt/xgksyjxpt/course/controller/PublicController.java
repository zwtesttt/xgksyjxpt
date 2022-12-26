package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;

import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PublicController {


    //注入Containerservice对象
    @Autowired
    private ContainerService containerService;


    //注入dockerservice对象
    @Autowired
    private DockerService dockerService;

    @Resource
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private StudentService studentService;



    /**
     * 根据容器id查询容器ip
     */
    @GetMapping("/getIp")
    public Object getIp(String id){
//        ReturnObject re=new ReturnObject();
        String networkName=DockerConfig.DOCKER_NETWORK_NAME;
        String ip=dockerService.getIp(id,networkName);
//        if (ip!=null){
//
//        }
        return ip;
    }
    /**
     * 根据镜像名和学生id创建容器返回容器id
     */
    @PostMapping("/createContain")
    public Object createContain(String imagesName,String stuId,String testid){
        ReturnObject re=new ReturnObject();
        try {
            String id= dockerService.createQueryId(imagesName,stuId,DockerConfig.DOCKER_NETWORK_NAME,testid);
            if (id!=null){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("运行成功");
                re.setData(id);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("运行容器失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("运行容器失败");
        }
        return re;
    }



    //查询学生头像链接
    @GetMapping("/selectStuHead")
    public String selectStuHead(String sid){
        String url = studentService.selectStuHeadUrl(sid);
//        截取字符串,去除url中的group信息
        String[] str=url.split("/",2);
        return str[1];
    }
    /**
     * 根据容器id获取容器名称
     */
    @GetMapping("/getContainerName")
    public String getContainerName(String cid){
        return dockerService.selectContainersName(cid);
    }

}
