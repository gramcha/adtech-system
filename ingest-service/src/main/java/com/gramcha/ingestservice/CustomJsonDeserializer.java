/**
 * @author gramcha
 * 08-Jul-2018 3:42:20 PM
 * 
 */
package com.gramcha.ingestservice;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer<T> extends JsonDeserializer<T> {
	public CustomJsonDeserializer() {
		// defaults from superclass
		super();

		// add our packages
		this.addTrustedPackages("com.gramcha.ingestservice.entities");
	}
}
