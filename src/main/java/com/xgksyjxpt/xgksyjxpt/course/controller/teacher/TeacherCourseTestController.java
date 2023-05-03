package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminClassService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    private AdminClassService adminClassService;

    @Resource
    private TeacherService teacherService;

    /**
     * 查询当前所有实验可用镜像
     */
    @GetMapping("/queryTestUseImages")
    @ApiOperation("查询当前所有实验可用镜像")
    public List<CourseTestImages> queryTestUseImages(){
        List<CourseTestImages> list=courseService.selectAllImagesName(null,null,null);
        return list;
    }
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
                courseTest.setTestId(UuidUtil.getUUID());
                //课程号
                courseTest.setCid((String) map.get("cid"));
                //实验名
                courseTest.setTestName((String) map.get("testName"));
                //使用镜像名
                courseTest.setTestImageName((String) map.get("testImageName"));
                //开始时间
                courseTest.setTestStartTime((String) map.get("testStartTime"));
                //结束时间
                courseTest.setTestEndTime((String) map.get("testEndTime"));
                //描述
                courseTest.setTestDescription((String) map.get("testDescription"));
                courseTest.setChapterId((Integer) map.get("chapterId"));
                //保存班级名
                List<String> classList= (List<String>) map.get("classList");
                String[] classNames=new String[classList.size()];
                int c=0;
                for (String s:classList
                     ) {
                    if (adminClassService.selectClassNameByClassName(s)!=null){
                        classNames[c++]=s;
                    }
                }
                if (c==classList.size()){
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
                        }else if(DateUtil.getDate(courseTest.getTestEndTime()).compareTo(DateUtil.getDate(t.getCourseEnd()))>0){
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("实验结束时间不能超过课程结束时间(课程结束时间："+t.getCourseEnd()+")");
                            //实验开始时间不能实验结束时间
                        }else if(DateUtil.getDate(courseTest.getTestEndTime()).compareTo(DateUtil.getDate(courseTest.getTestStartTime()))<0){
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("实验开始时间不能超过实验结束时间");
                        }else{
                            //添加课程实验
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
                                    st.setTest_id(courseTest.getTestId());
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
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("班级名错误，请重试");
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
     * 修改实验文档
     */
    @PostMapping("/modifyCourseTestDoc")
    @ApiOperation("修改实验文档")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="testId",value="实验id",dataType="string",required = true),
            @ApiImplicitParam(name="newText",value="文档",dataType="string",required = true)
    })
    public Object modifyCourseTest(@RequestBody UpdateCourseTestDocJson updateCourseTestDocJson){
        ReturnObject re=new ReturnObject();
        try {
            if (updateCourseTestDocJson!=null){
                int stu=courseTestService.updayeCourseTestDocByTestId(updateCourseTestDocJson.getTestId(),updateCourseTestDocJson.getNewText());
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
     * 修改实验绑定班级
     */
    @PostMapping("/updateCourseTestByClassName")
    @ApiOperation("修改实验绑定班级")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="className",value="班级列表",dataType="list",required = true),
            @ApiImplicitParam(name="className",value="班级列表",dataType="list",required = true),
            @ApiImplicitParam(name="updateCourseTestClassJson",value="实体类（不用管）",dataType="json",required = false)
    })
    public Object updateStudentCourseByClassName(@RequestBody UpdateCourseTestClassJson updateCourseTestClassJson){
        ReturnObject re=new ReturnObject();
        try {
            if (updateCourseTestClassJson!=null){
                String testId=updateCourseTestClassJson.getTestId();
                List<String> classNames=updateCourseTestClassJson.getClassName();
                if (testId!=null&&classNames!=null&&classNames.size()!=0){
//                校验课程号
                    CourseTest courseTest=courseTestService.selectCourseTestByTestId(testId);
                    int count=0;
                    for (String className:classNames
                    ) {
                        if(adminClassService.selectClassNameByClassName(className)!=null){
                            count++;
                        }
                    }
                    if (count==classNames.size()){
                        if (courseTest!=null){
                            //                        验证课程状态
                            if ("已开始".equals(courseTest.getTest_status())){
                                String[] classNameArray=classNames.toArray(new String[classNames.size()]);
                                //修改实验班级
                                int stu=courseTestService.updateTestClassByTestIdAndClassNames(testId,classNameArray);
                                if (stu!=0){
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                                    re.setMessage("修改实验班级成功");
                                }else{
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("修改实验班级失败");
                                }
                            }else{
                                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                re.setMessage("该课程已结束，不允许修改实验班级");
                            }
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("实验不存在");
                        }
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("部分或全部班级不存在");
                    }

                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("实验id和班级名不能为空");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("实验id和班级名不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改实验班级失败");
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
     * 查询实验列表
     */
    @GetMapping("/queryCourseTest")
    @ApiOperation("查询实验列表")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="账号id",dataType="string",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object queryCourseTest(CourseTest courseTest, String id,Integer pageNum, Integer pageSize){
        ReturnObject re =new ReturnObject();

        if (id!=null){
            if (teacherService.selectNotDelTeacher(id) != null) {
                if (pageNum != null && pageSize != null) {
                    Map<String,Object> remap=new HashMap<>();
                    List<Map<String,Object>> relist=new ArrayList<>();
                    //查询实验
                    List<CourseTest> list=courseTestService.queryCourseTestByTidOrSid(id,null,courseTest,(pageNum-1)*pageSize,pageSize);
                    for (CourseTest ct:list
                    ) {
                        //封装实验信息对象
                        Map<String,Object> map=new HashMap<>();
                        //实验id
                        map.put("testId",ct.getTestId());
                        //实验名
                        map.put("testName",ct.getTestName());
                        //实验开始时间
                        map.put("testStartTime",ct.getTestStartTime());
                        //实验结束时间
                        map.put("testEndTime",ct.getTestEndTime());
                        //使用镜像名
                        map.put("testImageName",ct.getTestImageName());
                        //实验状态
                        map.put("testStatus",ct.getTest_status());
                        //实验描述
                        map.put("testDescription",ct.getTestDescription());
                        //章节
                        map.put("courseChapterId",ct.getChapterId());
                        //课程号
                        map.put("cid",ct.getCid());
                        map.put("classList",courseTestService.selectCourseTestClass(ct.getTestId()));

                        relist.add(map);
                    }
                    remap.put("testList",relist);
                    remap.put("total",courseTestService.queryCourseTestCountByTidOrSid(id,null,courseTest));
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("查询成功");
                    re.setData(remap);
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("查询失败，分页参数不能为空");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("查询失败，老师不存在");
            }

        }else {
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
        re.setMessage("查询失败，id不能为空");
        }
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
