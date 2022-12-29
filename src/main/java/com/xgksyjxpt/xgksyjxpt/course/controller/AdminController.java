package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员通用接口")
public class AdminController {


    /**
     * 访问管理员首页
     */
    @GetMapping("/toIndex")
    @ApiOperation("跳转管理员首页")
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }
    /**
     * 查询宿主机当前所有镜像名
     */
    @GetMapping("/queryImages")
    @ApiOperation("查询宿主机当前所有镜像名")
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
