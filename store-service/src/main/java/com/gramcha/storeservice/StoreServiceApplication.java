package com.gramcha.storeservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.DeliveryTracker;
import com.gramcha.storeservice.service.StoreService;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gramcha.*"})
public class StoreServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreServiceApplication.class, args);
	}
}
