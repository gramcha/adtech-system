/**
 * @author gramcha
 * 09-Jul-2018 10:01:39 PM
 * 
 */
package com.gramcha.storeservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.storeservice.repos.ClickTrackerRepository;
import com.gramcha.storeservice.repos.DeliveryTrackerRepository;
import com.gramcha.storeservice.repos.InstallTrackerRepository;

@Service
public class MongoStoreService implements StoreService {

	@Autowired
	DeliveryTrackerRepository deliveryRepo;
	
	@Autowired
	ClickTrackerRepository clickRepo;
	
	@Autowired
	InstallTrackerRepository installRepo;
	
	@Override
	public void saveTracker(DeliveryTracker tracker) {
		deliveryRepo.save(tracker);
	}
	
	@Override
	public void saveTracker(ClickTracker tracker) {
		clickRepo.save(tracker);
	}
	
	@Override
	public void saveTracker(InstallTracker tracker) {
		installRepo.save(tracker);
	}

}
