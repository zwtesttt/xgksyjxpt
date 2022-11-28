package com.xgksyjxpt.xgksyjxpt.login.controller;

import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.Admin;
import com.xgksyjxpt.xgksyjxpt.login.domain.JwtUitls;
import com.xgksyjxpt.xgksyjxpt.course.domain.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.AdminService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.TeacherService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class LoginController {
    @Autowired
    private JwtUitls jwtUitls;
    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ResponseBody
    public Object login(String id,String passwd){
//        过期时间,单位为秒
        int es=60*5;
        ReturnObject re=new ReturnObject();
        //对密码解密
        String depasswd= Base64Converter.decode(passwd);
        //截取id第一个字符，判断身份，s表示学生，t表示教师，r表示管理员登录
        String token=null;
        String fid=id.substring(0,1);
        if (fid.equals("s")){
            Student user= studentService.selectStudent(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getName());
                //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
            }
        } else if (fid.equals("t")) {
            Teacher user=teacherService.selectTeacher(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getTname());
                //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
            }
        } else if (fid.equals("r")) {
            Admin user=adminService.selectAdmin(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getName());
                //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
            }
        }
        if (token==null){
            re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("登录失败");
        }else{
            re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("登录成功");
            re.setData(token);
        }

        return re;
    }


}
