package com.xgksyjxpt.xgksyjxpt.login.controller;

import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.login.domain.JwtUitls;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public Object login(String id,String passwd){
//        过期时间,单位为秒
        int es=60*5;
        ReturnObject re=new ReturnObject();
        //截取id第一个字符，判断身份，s表示学生，t表示教师，r表示管理员登录
        String token=null;
        String fid=id.substring(0,1);
        if (fid.equals("s")){
            Student user= studentService.selectStudent(id);
            if (user!=null){
                if (passwordEncoder.matches(passwd,user.getPasswd())){
                    token= jwtUitls.createToken(id,user.getName());
                    //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                    redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("密码错误");
                    return re;
                }
            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("该用户不存在");
                return re;
            }
        } else if (fid.equals("t")) {
            Teacher user=teacherService.selectTeacher(id);
            if (user!=null){
                if(passwordEncoder.matches(passwd,user.getPasswd())){
                    token= jwtUitls.createToken(id,user.getTname());
                    //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                    redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("密码错误");
                    return re;
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("该用户不存在");
                return re;
            }
        } else if (fid.equals("r")) {
            Admin user=adminService.selectAdmin(id);
            if (user!=null){
                if(passwordEncoder.matches(passwd,user.getPasswd())){
                    token= jwtUitls.createToken(id,user.getName());
                    //登录成功后将token作为key,用户信息作为value保存到redis,5分钟过期
                    redisTemplate.opsForValue().set(token,user.toString(),Duration.ofSeconds(es));
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("密码错误");
                    return re;
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("该用户不存在");
                return re;
            }
        }
        if (token==null){
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("登录失败");
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("登录成功");
            re.setData(token);
        }
        return re;
    }
    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Object logout(HttpServletRequest request){
        ReturnObject re=new ReturnObject();
        String token=request.getHeader("token");
        //判断token是否为空
        if(token!=null){
            //到数据库移除token
            try {
                Boolean stu=redisTemplate.delete(token);
                if (stu==true){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("登出成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("登出失败");
                }
            }catch (Exception e){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("登出失败");
                e.printStackTrace();
            }
        }
        return re;
    }


}
