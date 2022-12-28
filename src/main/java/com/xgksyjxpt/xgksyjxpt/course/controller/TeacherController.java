package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FastdfsUtil fastdfsUtil;



    /**
     * 访问教师首页
     * @return
     */
    @GetMapping("/toIndex")
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
    public List<Map<String,String>> getStudents(){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Student> ss=studentService.queryStudents(null,null);
        for (Student s:ss) {
            //封装学生信息
            Map<String,String> map=new HashMap<>();
            map.put("stu_id",s.getStu_id());
            map.put("name",s.getName());
            list.add(map);
        }
        return list;
    }

    /**
     * 更新老师信息
     */
    @PostMapping("/updateTeacher")
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
     * 前端要先对密码使用base64加密算法加密
     */
    @PostMapping("/updatePass")
    public Object updatePass(String tid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (tid!=null){
            //核对学生旧密码
            //密码解密
            String oldpasswd=Base64Converter.decode(oldPass);
            String newpasswd=Base64Converter.decode(newPass);
            //查询旧密码
            String pass= teacherService.selectTeaPaawd(tid);
            if(oldpasswd.equals(pass)){
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
                re.setMessage("密码不匹配");
            }
        }
        return re;
    }
    /**
     * 上传图片
     */
    @PostMapping("/uploadImage")
    public String uploadImage(MultipartFile file,String cid,Integer chapterId,Integer sectionId){
        String url=null;
        try{
            //上传文件拿到url
           url=fastdfsUtil.uploadFile(file);
           //封装对象
            CourseSectionImage obj=new CourseSectionImage();
            obj.setChapter_id(chapterId);
            obj.setCid(cid);
            obj.setImage_url(url);
            obj.setSection_id(sectionId);
            //添加数据库记录
            courseService.uploadChapterImage(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 添加课程
     * @param course 课程信息
     * @param file 课程封面图片
     * @return
     */
    @PostMapping("/addCourse")
    public Object addCourse(Course course,MultipartFile file){
        ReturnObject re=new ReturnObject();

        try{
            if (course!=null&&file!=null){
//                验证教师号是否存在
                Teacher t=teacherService.selectTeacher(course.getTid());
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
