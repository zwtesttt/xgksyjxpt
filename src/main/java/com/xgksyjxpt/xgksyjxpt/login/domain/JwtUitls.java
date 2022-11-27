package com.xgksyjxpt.xgksyjxpt.login.domain;

import com.xgksyjxpt.xgksyjxpt.login.serivce.AdminService;
import com.xgksyjxpt.xgksyjxpt.login.serivce.StudentService;
import com.xgksyjxpt.xgksyjxpt.login.serivce.TeacherService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUitls {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 加密密钥
     */
    private static final String KEY = "xgksyjxpt";

    /**
     * 生成token
     * @param id    用户id
     * @param userName  用户名
     * @return
     */
    public String createToken(String id,String userName){
        Map<String,Object> header = new HashMap();
        header.put("typ","JWT");
        header.put("alg","HS256");
        //setID:用户ID
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:用户名
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setId(id)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,KEY);
        return builder.compact();
    }

    /**
     * 验证token是否有效
     * @param token  请求头中携带的token
     * @return  token验证结果  1-token认证通过；0-token认证失败
     */
    public int verify(String token){
        Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        int re=0;
        //判断redis中是否有这个token
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
            //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
            String id = claims.getId();
            //截取id第一个字符，判断身份
            String fid=id.substring(0,1);
            Object user=null;
            if (fid.equals("s")){
                user= studentService.selectStudent(id);
            } else if (fid.equals("t")) {
                user=teacherService.selectTeacher(id);
            } else if (fid.equals("r")) {
                user=adminService.selectAdmin(id);
            }
            if(user != null){
//                刷新token的时间为5分钟
                redisTemplate.expire(token,60*5,TimeUnit.SECONDS);
                re=1;
            }else{
                re=0;
            }
        }
        return re;
    }
}
