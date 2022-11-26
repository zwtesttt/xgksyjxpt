package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.course.serivce.DockerService;
import com.xgksyjxpt.xgksyjxpt.domain.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.InputStreamBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Sycontroller {

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
//        if (ip!=null){
//
//        }
        return ip;
    }
    /**
     * 根据镜像名和学生id创建容器返回容器id
     */
    @GetMapping("/createContain")
    @ResponseBody
    public Object createContain(String imagesName,String stuId){
        ReturnObject re=new ReturnObject();
        try {
            String id= dockerService.createQueryId(DockerConfig.DOCKER_API_URL,imagesName,imagesName+"-"+stuId,DockerConfig.DOCKER_NETWORK_NAME);
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
