package com.wsws.moduleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.wsws.moduleapi",
		"com.wsws.moduleapplication",
		"com.wsws.moduledomain"
})
public class ModuleApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ModuleApiApplication.class, args);
	}
}