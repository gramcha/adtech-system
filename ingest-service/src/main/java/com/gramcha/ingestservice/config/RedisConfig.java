/**
 * @author gramcha
 * 07-Jul-2018 4:02:06 PM
 * 
 */
package com.gramcha.ingestservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {
	@Autowired
	private Environment env;
	@SuppressWarnings("deprecation")
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory =  new JedisConnectionFactory();
		factory.setHostName(env.getProperty("spring.redis.host"));
		factory.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }    
}
