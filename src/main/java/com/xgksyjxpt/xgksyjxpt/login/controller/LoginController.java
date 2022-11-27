package com.xgksyjxpt.xgksyjxpt.login.controller;

import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.login.domain.Admin;
import com.xgksyjxpt.xgksyjxpt.login.domain.JwtUitls;
import com.xgksyjxpt.xgksyjxpt.login.domain.Student;
import com.xgksyjxpt.xgksyjxpt.login.domain.Teacher;
import com.xgksyjxpt.xgksyjxpt.login.serivce.AdminService;
import com.xgksyjxpt.xgksyjxpt.login.serivce.StudentService;
import com.xgksyjxpt.xgksyjxpt.login.serivce.TeacherService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        ReturnObject re=new ReturnObject();
        //对密码解密
        String depasswd= Base64Converter.decode(passwd);
        //截取id第一个字符，判断身份
        String token=null;
        String fid=id.substring(0,1);
        if (fid.equals("s")){
            Student user= studentService.selectStudent(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getName());
            }
        } else if (fid.equals("t")) {
            Teacher user=teacherService.selectTeacher(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getTname());
            }
        } else if (fid.equals("r")) {
            Admin user=adminService.selectAdmin(id);
            if (user!=null&&user.getPasswd().equals(depasswd)){
                token= jwtUitls.createToken(id,user.getName());
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
