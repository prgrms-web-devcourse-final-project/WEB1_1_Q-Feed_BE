package com.wsws.moduleapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("API 문서 제목")
                .version("v1.0")
                .description("API에 대한 설명");
        return new OpenAPI().info(info);
    }
}
