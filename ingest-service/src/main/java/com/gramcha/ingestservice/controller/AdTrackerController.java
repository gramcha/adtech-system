/**
 * @author gramcha
 * 07-Jul-2018 2:22:49 PM
 * 
 */
package com.gramcha.ingestservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.ingestservice.models.AdClickTrackerRequest;
import com.gramcha.ingestservice.models.AdDeliveryTrackerRequest;
import com.gramcha.ingestservice.models.AdInstallTrackerRequest;

@RestController
@RequestMapping("/ads")
public class AdTrackerController {

	@RequestMapping(value="/ping",method=RequestMethod.GET)
	public String ping() {
		return "ping pong";
	}
	
	@RequestMapping(value="/delivery", method = RequestMethod.POST)
	public ResponseEntity<?>  deliveryTracker(@RequestBody AdDeliveryTrackerRequest deliveryPayload) {
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	@RequestMapping(value="/click", method = RequestMethod.POST)
	public ResponseEntity<?> clickTracker(@RequestBody AdClickTrackerRequest clickPayload) {
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	@RequestMapping(value="/install", method = RequestMethod.POST)
	public ResponseEntity<?> installTracker(@RequestBody AdInstallTrackerRequest installPayload) {
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
}
