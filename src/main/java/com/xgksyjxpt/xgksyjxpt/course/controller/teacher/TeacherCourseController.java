package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.ClassName;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentCourse;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentCourseJson;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminClassService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseChapterService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseSectionService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师业务(课程)")
public class TeacherCourseController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FastdfsUtil fastdfsUtil;
    @Resource
    private AdminClassService adminClassService;

    /**
     * 添加课程
     * @param course 课程信息
     * @param file 课程封面图片
     * @return
     */
    @PostMapping("/addCourse")
    @ApiOperation(value="添加课程")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="图片流",dataType="file",required = true),
            @ApiImplicitParam(name="cid",value="课程号(不用带)",dataType="string",required = false),
            @ApiImplicitParam(name="course_status",value="课程状态(不用带)",dataType="string",required = false)
    })
    public Object addCourse(Course course,MultipartFile file){
        ReturnObject re=new ReturnObject();

        try{
            if (course!=null&&file!=null){
//                验证教师号是否存在
                Teacher t=teacherService.selectNotDelTeacher(course.getTid());
                if (t == null) {
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("教师不存在");
                }else{
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
                        //添加课程记录
                        //生成uuid作为课程号主键
                        course.setCid(UuidUtil.getUUID());
                        int stu=courseService.insertCourse(course);
                        if (stu!=0){
//                    课程添加成功后上传课程封面

                            //把课程封面url保存到数据库
                            //封装封面实体类
                            CourseHead ch=new CourseHead();
                            ch.setCid(course.getCid());
                            ch.setCourse_link(url);
//                    添加记录
                            courseService.uploadCourseHead(ch);
                            //添加成功把课程id返回
                            Map<String,Object> remap=new HashMap<>();
                            remap.put("courseId",course.getCid());
                            remap.put("courseStatus",courseService.selectCourseByCid(course.getCid()).getCourse_status());
                            re.setData(remap);
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                            re.setMessage("添加成功");
                        }else {
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("添加失败");
                        }
                    }else{
                        //移除上传的封面
                        fastdfsUtil.deleteFile(url);
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("课程封面上传失败");
                    }
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程信息或封面不能为空");
            }


        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }

        return re;
    }
    /**
     * 根据课程号删除课程
     */
    @DeleteMapping("/deleteCourse")
    @ApiOperation("根据课程号删除课程")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true)
    public Object deleteCourse(String cid){
        ReturnObject re=new ReturnObject();
        try {
            if (cid!=null){
                //验证课程号是否存在
                Course cc= courseService.selectCourseByCid(cid);
                if (cc!=null){
                    int stu=courseService.deleteCourse(cid);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("该课程不存在");
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
            @ApiImplicitParam(name="course_status",value="课程状态(不用带)",dataType="string",required = false)
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
    /**
     * 添加学生选课
     * 本接口跟添加课程一起使用,调用添加课程后拿到课程状态
     */
    @PostMapping("/setStudentCourse")
    @ApiOperation("添加学生选课")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="className",value="班级列表",dataType="list",required = true),
            @ApiImplicitParam(name="StudentCourseJson",value="实体类（不用管）",dataType="json",required = false)
    })
    public Object setStudentCourse(@RequestBody StudentCourseJson studentCourseJson){
        ReturnObject re=new ReturnObject();
        try {
            if (studentCourseJson!=null){
                String courseId=studentCourseJson.getCid();
                List<String> classNames=studentCourseJson.getClassName();
                if (courseId!=null&&classNames!=null&&classNames.size()!=0){
//                校验课程号
                    Course course=courseService.selectCourseByCid(courseId);
                    int count=0;
                    for (String className:classNames
                         ) {
                        if(adminClassService.selectClassNameByClassName(className)!=null){
                            count++;
                        }
                    }
                    if (count==classNames.size()){
                        if (course!=null){
//                        验证课程状态
                            if ("已开始".equals(course.getCourse_status())){
                                //班级名转存数组
                                String[] classNameList=classNames.toArray(new String[classNames.size()]);
                                //给学生绑定选课
                                //查询学号
                                List<String> stuIds=studentService.selectStudentIdByClassName(classNameList);
                                String[] stulist=stuIds.toArray(new String[classNames.size()]);
                                //添加选课
                                int stu=studentService.insertStudentCourseByCourseId(stulist,courseId);
                                if (stu!=0){
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                                    re.setMessage("选课成功");
                                }else{
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("选课失败");
                                }
                            }else{
                                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                re.setMessage("该课程已结束，不允许选课");
                            }
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("课程不存在");
                        }

                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("部分或全部班级不存在");
                    }

                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程号和班级名不能为空");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号和班级名不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("选课失败");
        }
        return re;
    }
    /**
     * 修改课程绑定班级
     */
    @PostMapping("/updateStudentCourseByClassName")
    @ApiOperation("修改课程绑定班级")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="className",value="班级列表",dataType="list",required = true),
            @ApiImplicitParam(name="StudentCourseJson",value="实体类（不用管）",dataType="json",required = false)
    })
    public Object updateStudentCourseByClassName(@RequestBody StudentCourseJson studentCourseJson){
        ReturnObject re=new ReturnObject();
        try {
            if (studentCourseJson!=null){
                String courseId=studentCourseJson.getCid();
                List<String> classNames=studentCourseJson.getClassName();
                if (courseId!=null&&classNames!=null&&classNames.size()!=0){
//                校验课程号
                    Course course=courseService.selectCourseByCid(courseId);
                    int count=0;
                    for (String className:classNames
                    ) {
                        if(adminClassService.selectClassNameByClassName(className)!=null){
                            count++;
                        }
                    }
                    if (count==classNames.size()){
                        if (course!=null){
    //                        验证课程状态
                            if ("已开始".equals(course.getCourse_status())){
                                String[] classNameArray=classNames.toArray(new String[classNames.size()]);
                                //修改选课
                                int stu=studentService.updateStudentCourseByCid(courseId,classNameArray);
                                if (stu!=0){
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                                    re.setMessage("修改选课成功");
                                }else{
                                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                    re.setMessage("修改选课失败");
                                }
                            }else{
                                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                                re.setMessage("该课程已结束，不允许修改选课");
                            }
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("课程不存在");
                        }
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("部分或全部班级不存在");
                    }

                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程号和班级名不能为空");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号和班级名不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改选课失败");
        }
        return re;
    }
    /**
     * 查询所有学生姓名
     * @return
     */
    @GetMapping("/getStudentsName")
    @ApiOperation(value = "查询所有学生姓名",hidden = true)
    public List<Map<String,String>> getStudents(){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Student> ss=studentService.queryStudents(null,null,null);
        for (Student s:ss) {
            //封装学生信息
            Map<String,String> map=new HashMap<>();
            map.put("sid",s.getSid());
            map.put("name",s.getName());
            list.add(map);
        }
        return list;
    }
    /**
     * 查询学生班级
     */
    @GetMapping("/getStudentClassName")
    @ApiOperation("查询所有学生班级")
    public List<String> getStudentClassName(){
        return studentService.selectStudentClassName();
    }

    /**
     * 查询指定老师课程信息
     */
    @GetMapping("/getTeacherCourse")
    @ApiOperation("查询指定老师课程信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="tid",value="教师号",dataType="string",required = true),
            @ApiImplicitParam(name="courseName",value="筛选课程名",dataType="string",required = true)
    })
    public Object getTeacherCourse(String tid,String courseName){
        ReturnObject re=new ReturnObject();
        if (tid!=null){
//            校验教师号
            if (teacherService.selectNotDelTeacher(tid) != null) {
                //查询该老师创建的课程
                List<Course> clist=courseService.selectCourseByTid(tid,courseName);
                if (clist.size()!=0){
                    List<Map<String,Object>> relist=new ArrayList<>();
                    Map<String,Object> map=null;
                    for (Course c:clist
                    ) {
                        map=new HashMap<>();
                        //查询课程封面url
                        map.put("courseHeadUrl",courseService.selectCourseHeadUrlByCid(c.getCid()).substring(7));
                        map.put("cid",c.getCid());
                        map.put("courseName",c.getCname());
                        map.put("description",c.getDescription());
                        relist.add(map);
                    }
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("查询成功");
                    re.setData(relist);
                }else{
                    //该老师没有创建课程
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("查询成功");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("该教师不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("教师号不能为空");
        }
        return re;
    }
    /**
     * 根据课程号查询课程信息
     */
    @GetMapping("/getCourseInfo")
    @ApiOperation("根据课程号查询课程信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true)
    public Object getCourseInfo(String cid){
        ReturnObject re=new ReturnObject();
        if (cid!=null){
//            校验课程号
            Course course=courseService.selectCourseByCid(cid);
            if (course != null) {
                    Map<String,Object> map=new HashMap<>();
                    //查询课程封面url
                    map.put("courseHeadUrl",courseService.selectCourseHeadUrlByCid(course.getCid()).substring(7));
                    map.put("cid",course.getCid());
                    map.put("courseName",course.getCname());
                    map.put("description",course.getDescription());
                    map.put("courseStartTIme",course.getCourseStart());
                    map.put("courseEndTIme",course.getCourseEnd());
                    map.put("xuefen",course.getXuefen());
                    map.put("courseStatus",course.getCourse_status());
                    map.put("teacherName",teacherService.selectNotDelTeacher(course.getTid()).getName());
                    map.put("tid",course.getTid());
                    map.put("classList",studentService.selectStudentCourseClassNameByCid(cid));
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("查询成功");
                    re.setData(map);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("该课程不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号不能为空");
        }
        return re;
    }
    /**
     * 修改课程封面
     */
    @PostMapping("/modifyCourseHead")
    @ApiOperation("修改课程封面")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="file",value="头像文件",dataType="file",required = true)
    })
    public Object modifyCourseHead(String cid,MultipartFile file){
        ReturnObject re=new ReturnObject();
        try {
            if (cid!=null&&file.isEmpty()){
//            验证课程号
                if (courseService.selectCourseByCid(cid)!=null){
                    //保存原来封面url
                    String oldurl=courseService.selectCourseHeadUrlByCid(cid);
                    //修改封面
                    String newurl=fastdfsUtil.uploadFile(file);
                    CourseHead courseHead=new CourseHead();
                    courseHead.setCourse_link(newurl);
                    courseHead.setCid(cid);
                    int stu=courseService.updateCourseHead(courseHead);
                    if (stu!=0){
                        //修改成功后删除服务器中旧封面
                        fastdfsUtil.deleteFile(oldurl);
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }
                }else{
                    re.setMessage("课程不存在");
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号或封面不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setMessage("修改失败");
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
        }

        return re;
    }
}
