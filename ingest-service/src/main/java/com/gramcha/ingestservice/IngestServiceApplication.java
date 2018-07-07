package com.gramcha.ingestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gramcha.*"})
public class IngestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngestServiceApplication.class, args);
	}
}
