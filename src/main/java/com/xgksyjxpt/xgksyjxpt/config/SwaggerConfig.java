package com.xgksyjxpt.xgksyjxpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 查看所有接口
 */
//@Configuration // 配置类
//@EnableSwagger2 // 开启 swagger2 的自动配置
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        // 创建一个 swagger 的 bean 实例
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.xgksyjxpt.xgksyjxpt.course.controller"))
                // 配置基本信息
                .paths(PathSelectors.any()).build()
                ;
    }

    // 基本信息设置
    private ApiInfo apiInfo() {
        Contact contact = new Contact(
                "张振威",
                "http://119.23.64.15/#/", // 作者姓名
                "2318266924@qq.com"); // 作者邮箱
        return new ApiInfoBuilder()
                .title("新工科实验教学平台-接口文档") // 标题
                .description("众里寻他千百度，慕然回首那人却在灯火阑珊处") // 描述
                .termsOfServiceUrl("http://119.23.64.15/#/") // 跳转连接
                .version("1.0") // 版本
                .contact(contact)
                .build();
    }

}
