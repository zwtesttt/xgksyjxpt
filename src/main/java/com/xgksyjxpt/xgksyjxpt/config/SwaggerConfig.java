package com.xgksyjxpt.xgksyjxpt.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@Configuration // 配置类
@EnableSwagger2 // 开启 swagger2 的自动配置
@ConditionalOnClass(Docket.class)
@EnableKnife4j
public class SwaggerConfig {
    //从配置文件读取该值
    @Value("${springfox.swagger2.enabled}")
    private Boolean swaggerEnabled;

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnabled)
                .groupName("SwaggerDemo")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    // 基本信息设置
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("新工科实验教学平台-接口文档") // 标题
                .version("1.0") // 版本
                .build();
    }

}
