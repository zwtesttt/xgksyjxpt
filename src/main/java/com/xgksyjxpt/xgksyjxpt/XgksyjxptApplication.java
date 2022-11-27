package com.xgksyjxpt.xgksyjxpt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan(basePackages = {"com.xgksyjxpt.xgksyjxpt.course.mapper","com.xgksyjxpt.xgksyjxpt.login.mapper"})//mapper所在的包
public class XgksyjxptApplication {
    public static void main(String[] args) {
        SpringApplication.run(XgksyjxptApplication.class, args);
    }
}
