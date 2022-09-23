package com.zs.config;

import io.swagger.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengshuai <441497343@qq.com>
 * @create 2022-07-22 11:58
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment) {
        // 是否在测试环境开启文档
        // Profiles profiles = Profiles.of("dev");
        // boolean isEnableSwagger = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("zengshuai")
                .apiInfo(this.apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zs.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact author = new Contact("曾帅", "", "");
        return new ApiInfo("ZBLOG 接口文档", "", "v2.0", "",
                            author, "", "", new ArrayList<>());
    }

}
