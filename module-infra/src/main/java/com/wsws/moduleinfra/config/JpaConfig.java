package com.wsws.moduleinfra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wsws.moduleinfra.repo") // infra 모듈의 JPA Repository 경로
@EntityScan(basePackages = "com.wsws.moduledomain")
public class JpaConfig {
}
