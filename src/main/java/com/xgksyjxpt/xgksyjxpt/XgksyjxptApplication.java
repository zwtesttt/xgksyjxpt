package com.xgksyjxpt.xgksyjxpt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

@SpringBootApplication
@EnableTransactionManagement //开启事务管理
@MapperScan(basePackages = {"com.xgksyjxpt.xgksyjxpt.course.mapper","com.xgksyjxpt.xgksyjxpt.login.mapper"})//mapper所在的包
public class XgksyjxptApplication {
    public static void main(String[] args) {
        SpringApplication.run(XgksyjxptApplication.class, args);
    }

}
