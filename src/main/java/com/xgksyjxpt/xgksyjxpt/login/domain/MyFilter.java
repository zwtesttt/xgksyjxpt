package com.xgksyjxpt.xgksyjxpt.login.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyFilter implements Filter {
    @Autowired
    private JwtUitls jwtUitls;
    //白名单请求
    private static final List<String> ALLOW_URI= Arrays.asList("/login","aaa","/xgksyjxpt/login");
//    //学生业务请求
//    private static final List<String> STU_URI= Arrays.asList("/xgksyjxpt/login");
//    //老师业务请求
//    private static final List<String> TEA_URI= Arrays.asList("/xgksyjxpt/login");
//    //管理员业务请求
//    private static final List<String> ADM_URI= Arrays.asList("/xgksyjxpt/login");


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();
        String url =  ((HttpServletRequest)servletRequest).getRequestURI();
        int verify=0;
        if(url != null){
            //登录请求直接放行
            if (ALLOW_URI.contains(url)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }else{
                //其他请求验证token
                String token = ((HttpServletRequest)servletRequest).getHeader("token");
                //判断token是否为空
                if(StringUtils.isNotBlank(token)){
                    //判断权限
                    if (jwtUitls.authPerm(token,url)) {
                        //token验证结果
                        verify  = jwtUitls.verify(token);
                        if(verify != TokenStatus.ALLOW_CODE){
                            if(verify == TokenStatus.NO_FOUND_CODE){
                                map.put("code",TokenStatus.NO_FOUND_CODE);
                                map.put("data","token验证失败");
                            } else if (verify==TokenStatus.NO_USER_CODE) {
                                map.put("code",TokenStatus.NO_USER_CODE);
                                map.put("data","token用户丢失");
                            }
                        }else if(verify  == TokenStatus.ALLOW_CODE){
                            //验证成功，放行
                            filterChain.doFilter(servletRequest,servletResponse);
                            return;
                        }
                    }else{
                        //token为空的返回
                        map.put("code",TokenStatus.NO_PREMISSIONS_CODE);
                        map.put("data","未授权");
                    }
                }else{
                    //token为空的返回
                    map.put("code",TokenStatus.NO_PREMISSIONS_CODE);
                    map.put("data","未授权");
                }
            }
            JSONObject jsonObject = new JSONObject(map);
            servletResponse.setContentType("application/json");
            //设置响应的编码
            servletResponse.setCharacterEncoding("utf-8");
            //响应
            PrintWriter pw=servletResponse.getWriter();
            pw.write(jsonObject.toString());
            pw.flush();
            pw.close();
        }
    }

}
