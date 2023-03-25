package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师业务(实验)")
public class TeacherCourseTestController {

    @Autowired
    private CourseTestService courseTestService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    /**
     * 添加课程实验
     * 因为要提交复杂的表单数据，所以选择json
     */
    @PostMapping("/addCourseTest")
    @ApiOperation("添加课程实验")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="map",value="json对象",dataType="json",required = true)
    public Object addCourseTest(@RequestBody Map<String,Object> map){
        ReturnObject re=new ReturnObject();
        try {
            if (map != null) {
                //封装实验对象
                CourseTest courseTest=new CourseTest();
                //生成uuid作为课程实验主键
                courseTest.setTest_id(UuidUtil.getUUID());
                //课程号
                courseTest.setCid((String) map.get("cid"));
                //实验名
                courseTest.setTest_name((String) map.get("testName"));
                //使用镜像名
                courseTest.setTest_image_name((String) map.get("testImageName"));
                //开始时间
                courseTest.setTest_start_time((String) map.get("testStartTime"));
                //结束时间
                courseTest.setTest_end_time((String) map.get("testEndTime"));
                //描述
                courseTest.setTest_description((String) map.get("testDescription"));
                //保存班级名
                List<String> classList= (List<String>) map.get("classList");
                String[] classNames=new String[classList.size()];
                int c=0;
                for (String s:classList
                     ) {
                    classNames[c++]=s;
                }
//                验证课程是否存在
                Course t = courseService.selectCourseByCid(courseTest.getCid());
                if (t == null) {
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                } else {
                    //判断课程状态
                    if ("已结束".equals(t.getCourse_status())){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("该课程已结束");
//                        实验结束时间不能超过课程结束时间
                    }else if(DateUtil.getDate(courseTest.getTest_end_time()).compareTo(DateUtil.getDate(t.getCourseEnd()))>0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("实验结束时间不能超过课程结束时间");
                        //实验开始时间不能实验结束时间
                    }else if(DateUtil.getDate(courseTest.getTest_end_time()).compareTo(DateUtil.getDate(courseTest.getTest_start_time()))<0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("实验开始时间不能超过实验结束时间");
                    }else{
                        //添加课程实验记录
                        int stu = courseTestService.insertCourseTest(courseTest);
                        if (stu != 0) {
//                            给指定班级学生添加实验
                            List<String> sids=studentService.selectStudentIdByClassName(classNames);
                            StudentTest[] sts=new StudentTest[sids.size()];
                            int i=0;
                            //获取学号
                            for (String sid:sids
                                 ) {
                                //封装学生实验对象
                                StudentTest st=new StudentTest();
                                st.setTest_id(courseTest.getTest_id());
                                st.setSid(sid);
                                st.setCid(courseTest.getCid());
                                sts[i++]=st;
                            }
                            //开始添加学生实验
                            int add=studentService.insertStudentTest(sts);
                            if (add==sts.length){
                                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                                re.setMessage("添加成功");
                            }
                        } else {
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("添加失败");
                        }
                    }
                }
            } else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("实验信息不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 修改实验信息
     */
    @PostMapping("/modifyCourseTest")
    @ApiOperation("修改实验信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号（不用带）",dataType="string",required = false),
            @ApiImplicitParam(name="test_status",value="实验状态（不用带）",dataType="string",required = false)
    })
    public Object modifyCourseTest(CourseTest courseTest){
        ReturnObject re=new ReturnObject();
        try {
            if (courseTest!=null){
                int stu=courseTestService.updateCourseTest(courseTest);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("修改成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("参数不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 删除课程实验
     */
    @DeleteMapping("/delCourseTest")
    @ApiOperation("删除课程实验")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="testId",value="实验id",dataType="string",required = true)
    public Object delCourseTest(String testId){
        ReturnObject re=new ReturnObject();
        try {
//            验证参数
            if (testId!=null){
                //删除实验
                int stu=courseTestService.deleteCourseTestByTestId(testId);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("实验id不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
    /**
     * 根据课程号查询实验列表
     */
    @GetMapping("/queryCourseTest")
    @ApiOperation("根据课程号查询实验列表")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object queryCourseTest(String cid,Integer pageNum,Integer pageSize){
        ReturnObject re =new ReturnObject();
        List<Map<String,Object>> relist=new ArrayList<>();
        if (cid!=null){
            //查询实验
            List<CourseTest> list=courseTestService.queryCourseTestByCid(null,cid,(pageNum-1)*pageSize,pageSize);
            for (CourseTest ct:list
            ) {
                //封装实验信息对象
                Map<String,Object> map=new HashMap<>();
                //实验id
                map.put("testId",ct.getTest_id());
                //实验名
                map.put("testName",ct.getTest_name());
                //实验开始时间
                map.put("testStartTime",ct.getTest_start_time());
                //实验结束时间
                map.put("testEndTime",ct.getTest_end_time());
                //使用镜像名
                map.put("testImageName",ct.getTest_image_name());
                //实验状态
                map.put("testStatus",ct.getTest_status());
                //实验描述
                map.put("testDescription",ct.getTest_description());
                relist.add(map);
            }
        }
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(relist);
        return re;
    }
    /**
     * 查询选择指定课程的班级名
     */
    @GetMapping("/getStudentCourseClassName")
    @ApiOperation("查询选择指定课程的班级名")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true)
    })
    public Object getStudentCourseClassName(String cid){
        ReturnObject re=new ReturnObject();
        if (cid!=null){
//            校验课程号
            if (courseService.selectCourseByCid(cid)!=null){
                List<String> list=studentService.selectStudentCourseClassNameByCid(cid);
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                re.setData(list);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号不能为空");
        }
        return re;
    }

}
