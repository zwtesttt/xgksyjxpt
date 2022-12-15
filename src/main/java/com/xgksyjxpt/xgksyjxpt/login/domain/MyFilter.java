package com.xgksyjxpt.xgksyjxpt.login.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import java.util.List;

//请求验证拦截器
@Component
public class MyFilter implements Filter {
    @Autowired
    private JwtUitls jwtUitls;
    //白名单请求
    private static final List<String> ALLOW_URI= Arrays.asList("/xgksyjxpt/login","/xgksyjxpt/swagger-ui.html");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //创建响应对象
        ReturnObject re =new ReturnObject();
        String url =  ((HttpServletRequest)servletRequest).getRequestURI();
        int verify=0;
        if(url != null){
            //如果是ws请求则单独验证
            if (url.contains("/ws")){
//                String wstoken=((HttpServletRequest)servletRequest).getHeader("Sec-WebSocket-Protocol");
//                //token验证结果
//                verify  = jwtUitls.verify(wstoken);
//                System.out.println("用户请求连接ws");
//                if(verify != TokenStatus.ALLOW_CODE){
//                    if(verify == TokenStatus.NO_FOUND_CODE){
//                        re.setCode(TokenStatus.NO_FOUND_CODE);
//                        re.setMessage("token验证失败");
//                    } else if (verify==TokenStatus.NO_USER_CODE) {
//                        re.setCode(TokenStatus.NO_USER_CODE);
//                        re.setMessage("token用户丢失");
//                    }
//                }else if(verify  == TokenStatus.ALLOW_CODE){
//                    //验证成功，放行
//                    filterChain.doFilter(servletRequest,servletResponse);
//                    return;
//                }
//                如果是ws请求直接放行
                filterChain.doFilter(servletRequest,servletResponse);
                return;
                //登录请求直接放行
            }else if (ALLOW_URI.contains(url)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }else{
                //其他请求验证token
                String token = ((HttpServletRequest)servletRequest).getHeader("token");
                //判断token是否为空
                if(StringUtils.isNotBlank(token)){
//                    判断请求中是否带有角色前缀，没有前缀说明是公共接口
                    if(url.contains("/student/")||url.contains("/teacher/")||url.contains("/admin/")){
                        //判断权限
                        if (jwtUitls.authPerm(token,url)) {
                            //token验证结果
                            verify  = jwtUitls.verify(token);
                            if(verify != TokenStatus.ALLOW_CODE){
                                if(verify == TokenStatus.NO_FOUND_CODE){
                                    re.setCode(TokenStatus.NO_FOUND_CODE);
                                    re.setMessage("token验证失败");
                                } else if (verify==TokenStatus.NO_USER_CODE) {
                                    re.setCode(TokenStatus.NO_USER_CODE);
                                    re.setMessage("token用户丢失");
                                }
                            }else if(verify  == TokenStatus.ALLOW_CODE){
                                //验证成功，放行
                                filterChain.doFilter(servletRequest,servletResponse);
                                return;
                            }
                        }else{
                            //token为空的返回
                            re.setCode(TokenStatus.NO_PREMISSIONS_CODE);
                            re.setMessage("未授权");
                        }
                    }else{
                        //token验证结果
                        verify  = jwtUitls.verify(token);
                        if(verify != TokenStatus.ALLOW_CODE){
                            if(verify == TokenStatus.NO_FOUND_CODE){
                                re.setCode(TokenStatus.NO_FOUND_CODE);
                                re.setMessage("token验证失败");
                            } else if (verify==TokenStatus.NO_USER_CODE) {
                                re.setCode(TokenStatus.NO_USER_CODE);
                                re.setMessage("token用户丢失");
                            }
                        }else if(verify  == TokenStatus.ALLOW_CODE){
                            //验证成功，放行
                            filterChain.doFilter(servletRequest,servletResponse);
                            return;
                        }
                    }
                }else{
                    //token为空的返回
                    re.setCode(TokenStatus.NO_PREMISSIONS_CODE);
                    re.setMessage("未授权");
                }
            }


            ObjectMapper om= new ObjectMapper();
            String js=om.writeValueAsString(re);

            servletResponse.setContentType("application/json");
            //设置响应的编码
            servletResponse.setCharacterEncoding("utf-8");
            //响应
            PrintWriter pw=servletResponse.getWriter();
            pw.write(js);
            pw.flush();
            pw.close();
        }
    }

}
