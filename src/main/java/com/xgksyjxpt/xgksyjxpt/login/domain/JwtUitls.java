package com.xgksyjxpt.xgksyjxpt.login.domain;

import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.IdentityPermissionsService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUitls {

    @Autowired
    private AdminService adminService;

    @Resource
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Resource
    private IdentityPermissionsService identityPermissionsService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 加密密钥
     */
    private static final String KEY = "xgksyjxpt";

    /**
     * 生成token
     * @param id    用户id
     * @param identity  用户名
     * @return
     */
    public String createToken(String id,String identity){
        Map<String,Object> header = new HashMap();
        header.put("typ","JWT");
        header.put("alg","HS256");
        //setID:用户ID
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:用户名
        //setIssuedAt:token创建时间
        //signWith:加密方式
        //把用户身份存进token里
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setId(id)
                .setSubject(identity)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,KEY);
        return builder.compact();
    }

    /**
     * 验证token是否有效
     * @param token  请求头中携带的token
     * @return  token验证结果
     */
    public int verify(String token){
        int re=TokenStatus.NO_FOUND_CODE;
        Claims claims=null;
        try {
             claims= Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        }catch (Exception e){
            re=TokenStatus.NO_FOUND_CODE;
            e.printStackTrace();
        }

        //判断redis中是否有这个token
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
            //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
            String id = claims.getId();
            //截取id第一个字符，判断身份
            String identity=claims.getSubject();
            Object user=null;
            if (identity.equals("teacher")) {
                user=teacherService.selectNotDelTeacher(id);
            } else if (identity.equals("admin")||identity.equals("superadmin")) {
                user=adminService.selectNotDelAdmin(id);
            }else{
                user= studentService.selectNotDelStudent(id);
            }
            if(user != null){
//                刷新token的时间为一天
                redisTemplate.expire(token,60*60*24,TimeUnit.SECONDS);
                re=TokenStatus.ALLOW_CODE;
            }else{
                re=TokenStatus.NO_USER_CODE;
            }
        }
        return re;
    }

    /**
     * 判断是否有权限访问该url
     */
    public Boolean authPerm(String token,String url){
        Claims claims=null;
        boolean stu=false;
        try {
            //        解析token
            claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            //判断redis中是否有这个token
            if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
                //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
                String id = claims.getId();
                //判断身份
                String identity=claims.getSubject();
                //查询当前角色的权限
                List<String> identityList=identityPermissionsService.selectIdentityPermissions(identity);
//            如果角色为teacher并且访问的是学生的接口，则返回true
                if (identityList.contains("学生业务")&&url.contains("/student/")){
                    stu=true;
//                如果角色为teacher并且访问的是老师的接口，则返回true
                } else if (identityList.contains("老师业务")&&url.contains("/teacher/")) {
                    stu=true;
//                如果角色为管理员并且访问的是管理员的接口，则返回true
                } else if (adminService.selectNotDelAdmin(id)!=null&&url.contains("/admin/")) {
                    stu=true;
                } else if (identityList.contains("服务器管理")&&url.contains("/serverManage/")) {
                    stu=true;
                }else if (identityList.contains("课程管理")&&url.contains("/courseManage/")) {
                    stu=true;
                }else if (identityList.contains("老师管理")&&url.contains("/teacherManage/")) {
                    stu=true;
                }else if (identityList.contains("学生管理")&&url.contains("/studentManage/")) {
                    stu=true;
                }else if (identityList.contains("角色管理")&&url.contains("/identityManage/")) {
                    stu=true;
                }else if (identityList.contains("管理员管理")&&url.contains("/adminManage/")) {
                    stu=true;
                }else if (identityList.contains("班级管理")&&url.contains("/classManage/")) {
                    stu=true;
                }
            }
        }catch (MalformedJwtException e){
            e.printStackTrace();
            stu=false;
        }
        return stu;
    }
}
