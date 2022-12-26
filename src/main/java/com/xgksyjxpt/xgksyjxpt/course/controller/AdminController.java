package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private DockerService dockerService;
    @Autowired
    private CourseService courseService;

    /**
     * 访问管理员首页
     */
    @GetMapping("/toIndex")
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }

    /**
     * 批量添加学生
     */
    @PostMapping("/addStudents")
    public Object addStudents(@RequestBody Student[] students){
        ReturnObject re =new ReturnObject();
        int flag=0;
        try {
            if (students.length!=0){
                //验证学号
                for (Student s:students
                ) {
                    if (!"s".equals(s.getStu_id().substring(0,1))){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("学号格式错误");
                        break;
                    }
                    if(studentService.selectStuPass(s.getStu_id())!=null){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("学号"+s.getStu_id()+"已存在");
                        break;
                    }
                }
                if (flag==0){
                    for (Student s:students) {
                        //密码解密
                        String passwd = Base64Converter.decode(s.getPasswd());
                        s.setPasswd(passwd);
                    }
//                添加记录
                    int stu=studentService.insertStudent(students);
                    if (stu==students.length){
                        //添加学生后设置默认头像
                        //注意，要先上传头像到文件系统，初始化的时候就要操作
                        for (Student s:students
                        ) {
                            StudentHead sh=new StudentHead();
                            sh.setStu_id(s.getStu_id());
                            sh.setHead_link(HeadUrl.DEFAULT_STU_HEAD);
                            studentService.uploadStuHead(sh);
                        }
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }

    /**
     * 查询所有学生信息,包括密码
     * @return
     */
    @GetMapping("/getStudents")
    public List<Map<String,String>> getStudents(Integer pageNum,Integer pageSize){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Student> ss=studentService.queryStudents((pageNum-1)*pageSize,pageSize);
        for (Student s:ss) {
            //封装学生信息
            Map<String,String> map=new HashMap<>();
            map.put("stu_id",s.getStu_id());
            map.put("name",s.getName());
            map.put("sex",s.getSex());
            map.put("age",s.getAge()+"");
//            对密码进行加密后发送到前端
            map.put("passwd",Base64Converter.encode(s.getPasswd()));
            list.add(map);
        }
        return list;
    }
    /**
     * 查询所有老师信息,包括密码
     * @return
     */
    @GetMapping("/getTeachers")
    public List<Map<String,String>> getTeachers(Integer pageNum,Integer pageSize){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Teacher> ss=teacherService.queryAllTeacher((pageNum-1)*pageSize,pageSize);
        for (Teacher s:ss) {
            //封装学生信息
            Map<String,String> map=new HashMap<>();
            map.put("stu_id",s.getTid());
            map.put("name",s.getTname());
            map.put("sex",s.getSex());
            map.put("age",s.getAge()+"");
//            对密码进行加密后发送到前端
            map.put("passwd",Base64Converter.encode(s.getPasswd()));
            list.add(map);
        }
        return list;
    }


    /**
     * 添加学生
     */
    @PostMapping("/addStudent")
    public Object addStudents(Student student){
        ReturnObject re =new ReturnObject();
        try {
            //验证学号
            if ("s".equals(student.getStu_id().substring(0,1))){
                //密码解密
                String passwd = Base64Converter.decode(student.getPasswd());
                student.setPasswd(passwd);
                int stu=studentService.insertStudentOne(student);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("添加成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("添加失败");
                }
            }else if(studentService.selectStudent(student.getStu_id())!=null){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学号"+student.getStu_id()+"已存在");
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学号格式错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 批量添加老师
     */
    @PostMapping("/addTeachers")
    public Object addStudents(@RequestBody Teacher[] teachers){
        ReturnObject re =new ReturnObject();
        int flag=0;
        try {
            if (teachers.length!=0){
                //验证教师号
                for (Teacher s:teachers
                ) {
                    //判断教师号第一个字符
                    if(!"t".equals(s.getTid().substring(0,1))){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("教师号格式错误失败");
                        break;
                    }
                    //判断教师号是否存在
                    if(teacherService.selectTeacher(s.getTid())!=null){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("教师号"+s.getTid()+"已存在");
                        break;
                    }
                }
                if(flag==0){
                    for (Teacher s:teachers) {
                        //密码解密
                        String passwd = Base64Converter.decode(s.getPasswd());
                        s.setPasswd(passwd);
                    }
//                添加记录
                    int stu=teacherService.addTeachers(teachers);
                    if (stu==teachers.length){
                        //添加教师后设置默认头像
                        //注意，要先上传头像到文件系统，初始化的时候就要操作
                        for (Teacher s:teachers
                        ) {
                            TeacherHead sh=new TeacherHead();
                            sh.setTid(s.getTid());
                            sh.setHead_link(HeadUrl.DEFAULT_TEA_HEAD);
                            teacherService.uploadTeaHead(sh);
                        }
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 更新学生信息
     */
    @PostMapping("/updateStudent")
    public Object updateStudent(Student student){
        ReturnObject re=new ReturnObject();
        try {
            //密码解密
            String passwd = Base64Converter.decode(student.getPasswd());
            student.setPasswd(passwd);
            int stu= studentService.updateStudent(student);
            if (stu!=0){
                re.setCode(1);
                re.setMessage("修改成功");
            }else{
                re.setCode(0);
                re.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(0);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 更新老师信息
     */
    @PostMapping("/updateTeacher")
    public Object updateStudent(Teacher teacher){
        ReturnObject re=new ReturnObject();
        try {
            //密码解密
            String passwd = Base64Converter.decode(teacher.getPasswd());
            teacher.setPasswd(passwd);
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
     * 批量删除学生
     */
    @DeleteMapping("/deleteStudents")
    public Object deleteStudents(@RequestBody String[] stuIds){
        ReturnObject re=new ReturnObject();
        try {
            if (stuIds.length!=0){
                int stucount=0;
                //验证学生是否存在
                for (String sid:stuIds
                     ) {
                    Student ss=studentService.selectStudent(sid);
                    if (ss!=null) {stucount++;}
                }
                //如果列表内所有学生都存在才删除
                if (stucount==stuIds.length){
                    //                提前保存学生头像url和容器id
                    List<String> headurl=new ArrayList<>();
                    List<String> cids=new ArrayList<>();
                    for (String sid:stuIds
                    ) {
                        //查询学生头像url
                        String url=studentService.selectStuHeadUrl(sid);
                        //排除默认头像
                        if (!HeadUrl.DEFAULT_STU_HEAD.equals(url)){
                            headurl.add(url);
                        }
                        //查询学生容器id
                        List<String> cid=containerService.selectStuContainerId(sid);
                        if (cid.size()!=0){
                            for (String c:cid
                            ) {
                                cids.add(c);
                            }
                        }
                    }
//                开始删除学生
                    int stu=studentService.deleteStudents(stuIds);
                    if (stu!=0){
                        //删除文件系统中的学生头像
                        for (String url:headurl
                        ) {
                            fastdfsUtil.deleteFile(url);
                        }
                        //停止学生容器
                        int rs=dockerService.removeContainers(cids);
                        if (rs==cids.size()){
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                            re.setMessage("删除成功");
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("容器移除失败");
                        }
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("一个或多个学号不存在,请检查学号是否正确");
                }

            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("删除列表为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }

        return re;
    }
    /**
     * 批量删除老师
     */
    @DeleteMapping("/deleteTeachers")
    public Object deleteTeachers(@RequestBody String[] tids){
        ReturnObject re=new ReturnObject();
        try {
            if (tids.length!=0){
                int teacount=0;
                //验证老师是否存在
                for (String tid:tids
                ) {
                    Teacher ss=teacherService.selectTeacher(tid);
                    if (ss!=null) {teacount++;}
                }
                //如果列表内所有老师都存在才删除
                if (teacount==tids.length){
                    //                提前保存老师头像url
                    List<String> headurl=new ArrayList<>();
                    for (String tid:tids
                    ) {
                        //查询老师头像url
                        String url=teacherService.selectTeaHeadUrl(tid);
                        //排除默认头像
                        if (!HeadUrl.DEFAULT_TEA_HEAD.equals(url)){
                            headurl.add(url);
                        }
                    }
//                开始删除老师
                    int stu=teacherService.deleteTeachers(tids);
                    if (stu!=0){
                        //删除文件系统中的老师头像
                        for (String url:headurl
                        ) {
                            fastdfsUtil.deleteFile(url);
                        }
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("一个或多个教师不存在,请检查教师号是否正确");
                }
                }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("删除列表为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }

        return re;
    }
    /**
     * 查询服务器镜像列表
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
    /**
     * 根据课程号批量删除课程
     */
    @DeleteMapping("/deleteCourses")
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
     * 查询所有课程信息
     */
    @GetMapping("/queryAllCourse")
    public Object queryAllCourse(Integer pageNum,Integer pageSize){
        ReturnObject re=new ReturnObject();
        List<Map<String,Object>> relist=new ArrayList<>();
        //获取所有课程信息
        List<Course> clist=courseService.queryAllCourse((pageNum-1)*pageSize,pageSize);
        for (Course c:clist
             ) {
            //封装课程信息对象
            Map<String,Object> map=new HashMap<>();
            //查询课程号
            map.put("cid",c.getCid());
            //查询课程名称
            map.put("cname",c.getCname());
            //查询老师名称
            map.put("tname",teacherService.selectTeacher(c.getTid()).getTname());
            //开始时间
            map.put("startTime",c.getCourse_start());
            //结束时间
            map.put("endTime",c.getCourse_end());
            //学生人数
            map.put("stuCount",studentService.queryStuCourseByCid(c.getCid()));
            //实验数量
            map.put("testCount",courseService.selectCourseTestIdByCid(c.getCid()).size());
            //学分
            map.put("xuefen",c.getXuefen());
            relist.add(map);
        }
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(relist);

        return re;
    }
    /**
     * 根据课程号查询实验列表
     */
    @GetMapping("queryCourseTest")
    public Object queryCourseTest(String cid,Integer pageNum,Integer pageSize){
        ReturnObject re =new ReturnObject();
        List<Map<String,Object>> relist=new ArrayList<>();
        if (cid!=null){
            List<CourseTest> list=courseService.queryCourseTestByCid(cid,(pageNum-1)*pageSize,pageSize);
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
                //容器数量
                map.put("containerCount",containerService.selectContainerIdByTestId(ct.getTest_id()).size());
                relist.add(map);
            }

        }
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(relist);
        return re;
    }
    /**
     * 查询实验下容器列表
     */


}
