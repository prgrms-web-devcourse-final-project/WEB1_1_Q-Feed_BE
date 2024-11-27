package com.wsws.moduleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.wsws.moduleapplication",
		"com.wsws.moduledomain",
		"com.wsws.moduleinfra",
		"com.wsws.modulecommon",
		"com.wsws.modulesecurity"
})
public class ModuleApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ModuleApiApplication.class, args);
	}
}