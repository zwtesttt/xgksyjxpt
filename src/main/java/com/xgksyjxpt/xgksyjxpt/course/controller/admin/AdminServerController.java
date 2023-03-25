package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTestImages;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/serverManage")
@Api(tags = "服务器管理")
public class AdminServerController {

    @Autowired
    private CourseService courseService;

    /**
     * 查询服务器当前所有镜像名
     */
    @GetMapping("/queryImages")
    @ApiOperation("查询宿主机当前所有镜像名")
    public List<String> queryImages(){
        List<String> list=new ArrayList<>();
        //实例化dockerclient对象
        DockerClient dockerClient= DockerUtil.queryDockerClient(DockerConfig.DOCKER_API_URL);
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


    /**
     * 查询所有实验可用镜像
     */
    @GetMapping("/getCourseTestImages")
    @ApiOperation("查询所有实验可用镜像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="imagesName",value="筛选镜像名称",dataType="string",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页行数",dataType="int",required = true)
    })
    public Object getImages(String imageName,Integer pageNum,Integer pageSize){
        ReturnObject re =new ReturnObject();
        Map<String,Object> map=new HashMap<>();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        map.put("imageList",courseService.selectAllImagesName(imageName,(pageNum-1)*pageSize,pageSize));
        map.put("total",courseService.queryImageCount(imageName));
        re.setData(map);
        return re;
    }
    /**
     * 添加新可用镜像
     */
    @PostMapping("/addCourseTestImages")
    @ApiOperation("添加新可用镜像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="imagesName",value="镜像名称",dataType="string",required = true)
    })
    public Object addCourseTestImages(String imagesName){
        ReturnObject re =new ReturnObject();
        try {
//            封装可用镜像对象
            CourseTestImages courseTestImages=new CourseTestImages();
            courseTestImages.setImages_name(imagesName);
            courseTestImages.setAdd_time(DateUtil.getFomatDate(new Date()));
            //开始添加记录
            int stu=courseService.insertCourseTestImages(courseTestImages);
            if (stu!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("添加成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 批量删除实验可用镜像
     */
    @DeleteMapping("/deleteCourseTestImages")
    @ApiOperation("批量删除实验可用镜像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="imagesNames",value="镜像名列表",dataType="array",required = true)
    })
    public Object deleteCourseTestImages(@RequestBody String[] imagesNames){
        ReturnObject re =new ReturnObject();
        try {
            //添加记录
            int stu=courseService.deleteCourseTestImages(imagesNames);
            if (stu!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
    /**
     * 上传镜像
     */
    @ApiOperation("上传docker镜像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="镜像文件",dataType="file",required = true)
    })
    @PostMapping("/uploadImage")
    public Object uploadImage(MultipartFile file){
        ReturnObject re =new ReturnObject();
        try {
            //上传镜像
            DockerUtil.installImage(DockerUtil.queryDockerClient(DockerConfig.DOCKER_API_URL), file.getInputStream());
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("上传失败");
        }
        return re;
    }
}
