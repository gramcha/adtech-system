/**
 * @author gramcha
 * 18-Jul-2018 12:59:25 PM
 * 
 */
package com.gramcha.ingestservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import static springfox.documentation.builders.PathSelectors.regex;
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())        
//          .paths(regex("/ads.*"))        
          .build();                                           
    }
}
// reference article - http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api?utm_source=email&utm_medium=email&utm_campaign=series1-rest&tl_inbound=1&tl_target_all=1&tl_period_type=3
// localhost:8080/swagger-ui.html