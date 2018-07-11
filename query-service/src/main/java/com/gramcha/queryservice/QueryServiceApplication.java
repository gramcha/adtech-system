package com.gramcha.queryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gramcha.*"})
public class QueryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(QueryServiceApplication.class, args);
	}
}
