package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;

import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/courseManage")
@Api(tags = "课程管理")
public class AdminCourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseTestService courseTestService;

    /**
     * 查询所有课程信息
     */
    @GetMapping("/getCourses")
    @ApiOperation("查询所有课程信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int"),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int")
    })
    public Object queryAllCourse(Course course,Integer pageNum,Integer pageSize){
        ReturnObject re=new ReturnObject();
        Map<String,Object> remap=new HashMap<>();
        List<Map<String,Object>> relist=new ArrayList<>();
        List<Course> clist=null;
        //获取所有课程信息
        if(course==null){
            clist=courseService.queryAllCourse(null,(pageNum-1)*pageSize,pageSize);
        }else{
            clist=courseService.queryAllCourse(course,(pageNum-1)*pageSize,pageSize);
        }
        for (Course c:clist
        ) {
            //封装课程信息对象
            Map<String,Object> map=new HashMap<>();
            //封装课程号
            map.put("cid",c.getCid());
            //封装课程名称
            map.put("cname",c.getCname());
            //封装老师id
            map.put("tid",c.getTid());
            //开始时间
            map.put("startTime",c.getCourseStart());
            //结束时间
            map.put("endTime",c.getCourseEnd());
            //学生人数
            map.put("stuCount",studentService.queryStuCourseByCid(c.getCid()));
            //实验数量
            map.put("testCount",courseTestService.selectCourseTestIdByCid(c.getCid()).size());
            //学分
            map.put("xuefen",c.getXuefen());
            //课程状态
            map.put("courseStatus",c.getCourse_status());
            relist.add(map);
        }
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        //封装响应对象
        remap.put("couList",relist);
        remap.put("total",courseService.queryCourseCount(course));
        re.setData(remap);

        return re;
    }

    /**
     * 根据课程号批量删除课程
     */
    @DeleteMapping("/deleteCourses")
    @ApiOperation("根据课程号批量删除课程")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cids",value="课程id列表",dataType="array",required = true)
    })
    public Object deleteCourses(@RequestBody String[] cids){
        ReturnObject re=new ReturnObject();
        try {
            if (cids.length!=0){
                //验证课程号是否存在
                int c=0;
                for (String cid:cids
                ) {
                    Course cc= courseService.selectCourseByCid(cid);
                    if(cc!=null){
                        c++;
                    }
                }
                if (c==cids.length){
                    int stu=courseService.deleteCourses(cids);
                    if (stu==c){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }

                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("一个或多个课程不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }


    /**
     * 修改课程信息
     */
    @PostMapping("/modifyCourseInfo")
    @ApiOperation("修改课程信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="tid",value="教师号(不用带)",dataType="string",required = false),
            @ApiImplicitParam(name="course_status",value="课程状态(不用带)",dataType="string",required = false),
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true)
    })
    public Object modifyCourseInfo(Course course){
        ReturnObject re=new ReturnObject();
        try {
            if (course!=null){
                //校验课程号
                if (courseService.selectCourseByCid(course.getCid())!=null){
                    //修改信息
                    int stu=courseService.updateCourseInfo(course);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程信息不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }

}
