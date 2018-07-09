/**
 * @author gramcha
 * 08-Jul-2018 3:42:20 PM
 * 
 */
package com.gramcha.storeservice;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer<T> extends JsonDeserializer<T> {
	
	public CustomJsonDeserializer() {
		// defaults from superclass
		super();

		// add our packages
		this.addTrustedPackages("com.gramcha.entities");//and lombok.config file need to created to resolve Error: com.fasterxml.jackson.databind.exc.InvalidDefinitionException cannot construct instance of (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator) 
	}
}
