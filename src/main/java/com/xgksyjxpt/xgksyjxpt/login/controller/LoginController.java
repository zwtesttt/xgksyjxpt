package com.xgksyjxpt.xgksyjxpt.login.controller;

import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.IdentityPermissionsService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.login.domain.JwtUitls;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import io.swagger.annotations.*;
import org.apache.catalina.authenticator.SingleSignOnSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "登录接口")
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
    @Resource
    private IdentityPermissionsService identityPermissionsService;

    /**
     * 登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id",dataType="string",required = true),
            @ApiImplicitParam(name="passwd",value="密码",dataType="string",required = true)
    })
    public Object login(@RequestBody Map<String,String> map){
        String id=map.get("id");
        String passwd=map.get("passwd");
        String role=map.get("role");
        Map<String,Object> remap=new HashMap<>();
        Map<String,Object> userInfo=new HashMap<>();
        ReturnObject re=new ReturnObject();
        if(id!=null && !"".equals(id) && passwd!=null && !"".equals(passwd)){
            //        过期时间,单位为秒
            int es=60*5;
            String token=null;
            if ("student".equals(role) || "tutor".equals(role)){
                Student user= studentService.selectNotDelStudent(id);
                if (user!=null){
                    //密码匹配
                    if (passwordEncoder.matches(passwd,user.getPasswd())){
                        //封装用户信息
                        userInfo.put("id",user.getSid());
                        userInfo.put("name",user.getName());
                        String url=studentService.selectStuHeadUrl(user.getSid());
                        userInfo.put("head_url",url.substring(7));
                        //创建token
                        token= jwtUitls.createToken(id,user.getName());
                        userInfo.put("identity",user.getIdentity());
                        userInfo.put("identityPermissions",identityPermissionsService.selectIdentityPermissions(user.getIdentity()));
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
            } else if ("teacher".equals(role)) {
                Teacher user=teacherService.selectNotDelTeacher(id);
                if (user!=null){
                    if(passwordEncoder.matches(passwd,user.getPasswd())){
                        //封装用户信息
                        userInfo.put("id",user.getTid());
                        userInfo.put("name",user.getName());
                        String url= teacherService.selectTeaHeadUrl(user.getTid());
                        userInfo.put("head_url",url.substring(7));
                        userInfo.put("identity",user.getIdentity());
                        userInfo.put("identityPermissions",identityPermissionsService.selectIdentityPermissions(user.getIdentity()));
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
            } else if ("admin".equals(role) || "superadmin".equals(role)) {
                Admin user=adminService.selectNotDelAdmin(id);
                if (user!=null){
                    if(passwordEncoder.matches(passwd,user.getPasswd())){
                        //封装用户信息
                        userInfo.put("id",user.getRid());
                        userInfo.put("name",user.getName());
                        String url=adminService.selectAdminHeadUrl(user.getRid());
                        userInfo.put("head_url",url.substring(7));
                        userInfo.put("identity",user.getIdentity());
                        token= jwtUitls.createToken(id,user.getName());
                        userInfo.put("identityPermissions",identityPermissionsService.selectIdentityPermissions(user.getIdentity()));
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
                remap.put("token",token);
                remap.put("userInfo",userInfo);
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("登录成功");
                re.setData(remap);
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("用户名或密码不能为空");
        }


        return re;
    }
    /**
     * 退出登录
     */
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
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
