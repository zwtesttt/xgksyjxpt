package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.Student;
import com.xgksyjxpt.xgksyjxpt.course.serivce.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.AdminService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    /**
     * 访问管理员首页
     */
    @GetMapping("/toIndex")
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }

    /**
     * 批量添加学生
     */
    @PostMapping("/addStudents")
    public Object addStudents(@RequestBody Student[] students){
        ReturnObject re =new ReturnObject();
        try {
            int stu=studentService.insertStudent(students);
            if (stu==students.length){
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("添加成功");
            }else{
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }

    /**
     * 添加学生
     */
    @PostMapping("/addStudent")
    public Object addStudents(Student student){
        ReturnObject re =new ReturnObject();
        try {
            //密码解密
            String passwd = Base64Converter.decode(student.getPasswd());
            student.setPasswd(passwd);
            int stu=studentService.insertStudentOne(student);
            if (stu!=0){
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("添加成功");
            }else{
                re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
}
