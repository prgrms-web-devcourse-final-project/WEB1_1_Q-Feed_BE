package com.wsws.moduleapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";

        // Info 설정
        Info info = new Info()
                .title("API 문서 제목")
                .version("v1.0")
                .description("API에 대한 설명");

        // Security 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        List<Server> servers = new ArrayList<>();
        servers.add(new Server()
                .url("http://localhost:8080")
                .description("Local http server"));
        servers.add(new Server()
                .url("https://localhost:8080")
                .description("Local https server"));
        servers.add(new Server()
                .url("https://q-feed.n-e.kr")
                .description("https qfeed server"));
        servers.add(new Server()
                .url("http://q-feed.n-e.kr")
                .description("http qfeed server"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName, securityScheme))
                .servers(servers);
    }
}
