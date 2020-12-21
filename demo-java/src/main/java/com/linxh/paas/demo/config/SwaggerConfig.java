package com.linxh.paas.demo.config;

import com.fasterxml.classmate.TypeResolver;
import com.linxh.paas.demo.PaasMain;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(PaasMain.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .forCodeGeneration(true);
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Demo")
                .description("更多请咨询服务提供方 林")
                .contact(new Contact("林", "http://www.linxh.vip", "v.linxh@gmail.com"))
                .termsOfServiceUrl("http://www.linxh.vip")
                .version("0.1.0")
                .build();
    }

}
