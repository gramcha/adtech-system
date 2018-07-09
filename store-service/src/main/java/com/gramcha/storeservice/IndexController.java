/**
 * @author gramcha
 * 09-Jul-2018 10:19:19 PM
 * 
 */
package com.gramcha.storeservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.storeservice.service.StoreService;

@RestController
public class IndexController {
	@Autowired
	StoreService store;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	String saveData() {

		DeliveryTracker tracker = new DeliveryTracker(4483, "544cf0db-ba28-4c5f-8c9c-2bf11ee42988", new Date(),
				"Chrome", "iOS", "http://super-dooper-news.com");
		store.saveTracker(tracker);

		ClickTracker cTracker = new ClickTracker(4483, "544cf0db-ba28-4c5f-8c9c-2bf11ee42988", "Chrome", "iOS",
				"http://super-dooper-news.com", "fff54b83-49ff-476f-8bfb-2ec22b252c32", new Date());
		store.saveTracker(cTracker);

		InstallTracker iTracker = new InstallTracker(4483, "544cf0db-ba28-4c5f-8c9c-2bf11ee42988", "Chrome", "iOS",
				"http://super-dooper-news.com", "144cf0db-ba28-4c5f-8c9c-2bf11ee42988",
				"fff54b83-49ff-476f-8bfb-2ec22b252c32", new Date());
		store.saveTracker(iTracker);
		
		return "okay";
	}
}
