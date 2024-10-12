package com.linzi.daily.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Demo应用接口")
                        .version("1.0")
                        .description("接口文档")
                        .contact(new Contact().name("Lil").email("java_linzi@163.com"))
                );
    }
}
