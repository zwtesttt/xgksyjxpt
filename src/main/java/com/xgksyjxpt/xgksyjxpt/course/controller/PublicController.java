package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;

import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "公用接口")
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
    @ApiOperation("根据容器id查询容器ip")
    @ApiImplicitParam(name="id",value="容器id",dataType="string",required = true)
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
    @ApiOperation("根据镜像名和学生id创建容器返回容器id")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="imagesName",value="镜像名称",dataType="string",required = true),
            @ApiImplicitParam(name="stuId",value="学生id",dataType="string",required = true),
            @ApiImplicitParam(name="testId",value="实验id",dataType="string",required = true)
    })
    public Object createContain(String imagesName,String stuId,String testId){
        ReturnObject re=new ReturnObject();
        try {
            String id= dockerService.createQueryId(imagesName,stuId,DockerConfig.DOCKER_NETWORK_NAME,testId);
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
    @ApiOperation("查询学生头像链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true)
    })
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
    @ApiOperation("根据容器id获取容器名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="容器id",dataType="string",required = true)
    })
    public String getContainerName(String cid){
        return dockerService.selectContainersName(cid);
    }

}
