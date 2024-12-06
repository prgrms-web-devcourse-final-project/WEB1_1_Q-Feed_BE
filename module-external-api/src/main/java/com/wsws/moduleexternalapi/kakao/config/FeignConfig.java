package com.wsws.moduleexternalapi.kakao.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.wsws.moduleexternalapi.kakao")
public class FeignConfig {
}
