package com.xgksyjxpt.xgksyjxpt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})//排除security的登录验证
@EnableTransactionManagement //开启事务管理
@MapperScan(basePackages = {"com.xgksyjxpt.xgksyjxpt.course.mapper","com.xgksyjxpt.xgksyjxpt.login.mapper"})//mapper所在的包
public class XgksyjxptApplication {
    public static void main(String[] args) {
        SpringApplication.run(XgksyjxptApplication.class, args);
    }
    /**
     * 注入加密对象
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
