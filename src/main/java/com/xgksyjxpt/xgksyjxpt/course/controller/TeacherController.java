package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@Api(tags = "教师接口")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 访问教师首页
     * @return
     */
    @GetMapping("/toIndex")
    @ApiOperation(value = "访问教师首页",hidden = true)
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }

    /**
     * 查询所有学生姓名
     * @return
     */
    @GetMapping("/getStudentsName")
    @ApiOperation("查询所有学生姓名")
    public List<Map<String,String>> getStudents(){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Student> ss=studentService.queryStudents(null,null);
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
     * 添加学生选课
     */
    @PostMapping("/setStudentCourse")
    @ApiOperation("根据班级绑定学生课程")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="courseId",value="课程id",dataType="string",required = true),
            @ApiImplicitParam(name="classNames",value="班级数组",dataType="array",required = true)}
    )
    public Object setStudentCourse(@RequestBody Map<String,Object> map){
        ReturnObject re=new ReturnObject();
        try {
            if (map!=null){
                String courseId=(String)map.get("courseId");
                List<String> classNames= (List<String>) map.get("classNames");
                if (courseId!=null&&classNames!=null&&classNames.size()!=0){
//                校验课程号
                    Course course=courseService.selectCourseByCid(courseId);
                    if (course!=null){
//                        验证课程状态
                        if ("已开始".equals(course.getCourse_status())){
                            //班级名转存数组
                            int k=0;
                            String[] classNameList=new String[classNames.size()];
                            for (String className:classNames
                            ) {
                                classNameList[k++]=className;
                            }
                            //给学生绑定选课
                            //查询学号
                            List<String> stuIds=studentService.selectStudentIdByClassName(classNameList);
                            String[] stulist=new String[stuIds.size()];
                            int i=0;
//        学号转存数组
                            for (String sid:stuIds
                            ) {
                                stulist[i++]=sid;
                            }
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
     * 添加课程章节
     */
    @PostMapping("/addCourseChapter")
    @ApiOperation("添加课程章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addCourseChapter(CourseChapter courseChapter){
        ReturnObject re=new ReturnObject();
        try{
            if (courseChapter!=null){
                //验证课程号
                Course course=courseService.selectCourseByCid(courseChapter.getCid());
                if (course!=null){
                    int stu=courseService.insertCourseChapter(courseChapter);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("章节信息不能为空");
            }
        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 删除根据章节号课程章节
     */
    @DeleteMapping("/delCourseChapterByChapterId")
    @ApiOperation("删除根据章节号课程章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节号",dataType="string",required = true)
    })
    public Object delCourseChapterByChapterId(String cid,String chapterId){
        ReturnObject re=new ReturnObject();
        try {
            if (cid!=null && chapterId!=null){
                int stu=courseService.deleteCourseChapterByCidAndChapterId(cid,chapterId);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号或者章节号不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }

    /**
     * 查询课程章节
     */
    @GetMapping("/getCourseChapter")
    @ApiOperation("查询课程章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true)
    public Object getCourseChapter(String cid){
        ReturnObject re=new ReturnObject();
        if (cid!=null){
            if (courseService.selectCourseByCid(cid)!=null){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                re.setData(courseService.selectCourseChapter(cid));
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
    /**
     * 更新老师信息
     */
    @PostMapping("/updateTeacher")
    @ApiOperation("更新老师信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="passwd",value="密码(不用填)",dataType="string",required = false)
    public Object updateStudent(Teacher teacher){
        ReturnObject re=new ReturnObject();
        try {
            int stu= teacherService.updateTeacher(teacher);
            if (stu!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("修改成功");
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 修改老师密码
     */
    @PostMapping("/updatePass")
    @ApiOperation("修改老师密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="oldPass",value="旧密码",dataType="string",required = true),
            @ApiImplicitParam(name="tid",value="老师id",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updatePass(String tid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (tid!=null){
            //核对学生旧密码
            //新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
            //查询旧密码
            String pass= teacherService.selectTeaPaawd(tid);
            //核对密码
            if(passwordEncoder.matches(oldPass,pass)){
                try {
                    //开始修改密码
                    int stu=teacherService.updateTeaPasswd(tid,newpasswd);
//                   修改是否成功
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改失败");
                    }
                }catch (Exception e){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                    e.printStackTrace();
                }

            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("原密码不匹配");
            }
        }
        return re;
    }
    /**
     * 上传图片
     */
    @PostMapping("/uploadImage")
    @ApiOperation("上传图片")
    @ApiResponses(@ApiResponse(code = 200,response = String.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="图片流",dataType="multipartFile",required = true),
    })
    public String uploadImage(MultipartFile file){
        String url=null;
        try{
            //上传文件拿到url
           url=fastdfsUtil.uploadFile(file);
            url=url.substring(7);
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
    /**
     * 保存上传图片记录
     */
    @PostMapping("/saveSectionImage")
    @ApiOperation("保存上传图片记录")
    @ApiResponses(@ApiResponse(code = 200,response = String.class,message = "成功"))
    public Object uploadCourseSectionImage(@RequestBody Map<String,Object> map) {
        ReturnObject re=new ReturnObject();
        try {
            if (map!=null){
                CourseSectionImage obj=null;
                //封装对象
                //获取章节id
                Integer chapterId= (Integer) map.get("chapterId");
                //获取课程id
                String cid= (String) map.get("cid");
                //获取小节id
                Integer sectionId= (Integer) map.get("sectionId");
                //保存url
                List<String> urls= (List<String>) map.get("urls");
                int count=0;
                if (urls.size()!=0){
                    for (String url:urls
                     ) {
                        obj=new CourseSectionImage();
                        obj.setChapter_id(chapterId);
                        obj.setCid(cid);
                        obj.setImage_url(url);
                        obj.setSection_id(sectionId);
                        //添加数据库记录
                        int st=courseService.uploadChapterImage(obj);
                        if (st!=0){
                            count++;
                        }
                    }
                }
                if (count==urls.size()){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("保存成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("保存失败");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("保存失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("保存失败");
        }
        return re;
    }
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
            @ApiImplicitParam(name="file",value="图片流",dataType="file",required = true)
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
                    //添加课程记录
                    //生成uuid作为课程号主键
                    course.setCid(UuidUtil.getUUID());
                    int stu=courseService.insertCourse(course);
                    if (stu!=0){
//                    课程添加成功后上传课程封面
                        String url=fastdfsUtil.uploadFile(file);
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
//                        remap.put("")
                        re.setData(remap);
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else {
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
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
     * 添加课程实验
     */
    @PostMapping("/addCourseTest")
    @ApiOperation("添加课程实验")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addCourseTest(CourseTest courseTest){
        ReturnObject re=new ReturnObject();

        try {
            if (courseTest != null) {
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
                    }else if(DateUtil.getDate(courseTest.getTest_end_time()).compareTo(DateUtil.getDate(t.getCourse_end()))>0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("实验结束时间不能超过课程结束时间");
                    }else{
                        //添加课程实验记录
                        //生成uuid作为课程实验主键
                        courseTest.setTest_id(UuidUtil.getUUID());
                        int stu = courseService.insertCourseTest(courseTest);
                        if (stu != 0) {
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                            re.setMessage("添加成功");
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

}
