package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseStatus;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
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

    @Autowired
    private DockerService dockerService;

    @Autowired
    private CourseTestService courseTestService;

    @Autowired
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;



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
    @ApiOperation("根据镜像名和id创建容器返回容器id")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="imagesName",value="镜像名称",dataType="string",required = true),
            @ApiImplicitParam(name="id",value="id",dataType="string",required = true),
            @ApiImplicitParam(name="testId",value="实验id",dataType="string",required = true)
    })
    public Object createContain(String imagesName,String id,String testId){
        ReturnObject re=new ReturnObject();
        try {
            if (imagesName!=null&&id!=null&&testId!=null){
                //验证学号
                if (studentService.selectNotDelStudent(id)!=null||teacherService.selectNotDelTeacher(id)!=null){
                    CourseTest courseTest=courseTestService.selectCourseTestByTestId(testId);
                    if (courseTest!=null){
                        if (studentService.selectStudentTestBySidAndTestId(id,testId)!=null){
                            if (CourseStatus.COURSE_START.equals(courseTest.getTest_status())){
                                String conId= dockerService.createQueryId(imagesName,id,DockerConfig.DOCKER_NETWORK_NAME,testId);
                                if (conId!=null){
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                                    re.setMessage("运行成功");
                                    re.setData(conId);
                                }else{
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("运行容器失败");
                                }
                                }else if (CourseStatus.COURSE_END.equals(courseTest.getTest_status())) {
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("实验已结束");
                                } else if (CourseStatus.COURSE_NOT_START.equals(courseTest.getTest_status())) {
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("实验未开始");
                                }
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("找不到该实验");
                        }
                    }else {
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("实验不存在");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("学号不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("镜像名、账号、实验id不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("运行容器失败");
        }
        return re;
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
