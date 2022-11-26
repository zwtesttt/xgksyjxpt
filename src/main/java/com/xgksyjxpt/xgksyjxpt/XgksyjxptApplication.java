package com.xgksyjxpt.xgksyjxpt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan(basePackageClasses = com.xgksyjxpt.xgksyjxpt.course.mapper.ContainerMapper.class)//mapper所在的包
public class XgksyjxptApplication {
    public static void main(String[] args) {
        SpringApplication.run(XgksyjxptApplication.class, args);
    }
}
