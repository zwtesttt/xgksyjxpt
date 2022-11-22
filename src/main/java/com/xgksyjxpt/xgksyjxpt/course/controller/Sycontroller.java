package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.xgksyjxpt.xgksyjxpt.course.serivce.DockerService;
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

@Controller
public class Sycontroller {

    @Autowired
    private DockerService dockerService;

//
//    @GetMapping("/getWsId")
//    @ResponseBody
//    public String getWsId(){
//        String wsid="";
//
//        try{
//            //1.打开浏览器，创建HttpClient对象
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            //2.输入网址，发起请求创建HttpPost对象，设置url访问地址
//            HttpPost httpPost = new HttpPost("http://119.23.64.15/");
//
////        封装表单对象
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            //解决上传文件，文件名中文乱码问题
//            builder.setCharset(Charset.forName("utf-8"));
////        表单参数为文件流
//            ClassPathResource classpathResource = new ClassPathResource("7877");
//            InputStream in=classpathResource.getInputStream();
//            builder.addPart("privatekey", new InputStreamBody(in,"7877"));
//            builder.addTextBody("version","1.0");
//            builder.addTextBody("_xsrf","2|7179add8|9da5f94878a71c24b8e7bd141076b4d6|1668859276");
//            builder.addTextBody("hostname","172.19.0.4");
//            builder.addTextBody("username","root");
////        设置请求头cookie
//            httpPost.setHeader("Cookie","_xsrf=2|d296a41e|3e4af08edb4815e21b08b4d2b399bd10|1668859276");
//            httpPost.setEntity(builder.build());
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            //4.解析响应，获取数据
//            //判断状态码是否为200
//            if (response.getCode() == 200){
//                HttpEntity httpEntity = response.getEntity();
//                String content = EntityUtils.toString(httpEntity,"utf8");
//                System.out.println(content);
//                wsid=content;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return wsid;
//    }
    /**
     * 根据容器id查询容器ip
     */
    @GetMapping("/getIp")
    @ResponseBody
    public Object getIp(String id){
        ReturnObject re=new ReturnObject();
        String ip=null;
        String url="tcp://192.168.3.24:2375";
        String networkName="mynet1";
        ip=dockerService.getIp(id,url,networkName);
        if (ip!=null){

        }

        return ip;
    }
}
