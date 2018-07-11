package com.gramcha.storeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gramcha.*"})
public class StoreServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreServiceApplication.class, args);
	}
}
