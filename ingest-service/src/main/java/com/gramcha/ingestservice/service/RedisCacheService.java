/**
 * @author gramcha
 * 07-Jul-2018 4:58:07 PM
 * 
 */
package com.gramcha.ingestservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.ingestservice.entities.ClickTracker;
import com.gramcha.ingestservice.entities.DeliveryTracker;
import com.gramcha.ingestservice.entities.InstallTracker;
import com.gramcha.ingestservice.repos.AdClickTrackerRepository;
import com.gramcha.ingestservice.repos.AdDeliveryTrackerRepository;

@Service
public class RedisCacheService implements CacheService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	@Autowired
	AdDeliveryTrackerRepository deliveryTrackerRepo;

	@Autowired
	AdClickTrackerRepository clickTrackerRepo;
	@Override
	public void addDeliveryTrackerIntoCache(DeliveryTracker deliveryTracker) {
		deliveryTrackerRepo.save(deliveryTracker);
	}

	@Override
	public DeliveryTracker queryDeliveryTrackerFromCache(String deliveryId) {
		return deliveryTrackerRepo.findByDeliveryId(deliveryId);
	}

	@Override
	public void addClickTrackerIntoCache(ClickTracker clickTracker) {
		clickTrackerRepo.save(clickTracker);
	}

	@Override
	public ClickTracker queryClickTrackerFromCache(String clickId) {
		return clickTrackerRepo.findByClickId(clickId);
	}

	@Override
	public void addInstallTrackerIntoCache(InstallTracker deliveryTracker) {
		// TODO Auto-generated method stub
	}

	@Override
	public InstallTracker queryInstallFromCache(String installId) {
		// TODO Auto-generated method stub
		return null;
	}

}
