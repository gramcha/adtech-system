/**
 * @author gramcha
 * 09-Jul-2018 9:28:15 PM
 * 
 */
package com.gramcha.storeservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories({ "com.gramcha.storeservice.repos" })
public class MongoConfig {
	@Autowired
	private Environment env;

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {

		String hostName = env.getProperty("spring.data.mongodb.host");
		int portNumber = Integer.parseInt(env.getProperty("spring.data.mongodb.port"));
		// UserCredentials userCredentials = new UserCredentials("", "");
		MongoClient mongoClient = new MongoClient(hostName, portNumber);
		return new SimpleMongoDbFactory(mongoClient, "adtech");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;

	}
}
