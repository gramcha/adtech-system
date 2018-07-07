/**
 * @author gramcha
 * 07-Jul-2018 4:51:45 PM
 * 
 */
package com.gramcha.ingestservice.service;

import com.gramcha.ingestservice.entities.ClickTracker;
import com.gramcha.ingestservice.entities.DeliveryTracker;

public interface CacheService {
	void addDeliveryTrackerIntoCache(DeliveryTracker deliveryTracker);

	DeliveryTracker queryDeliveryTrackerFromCache(String deliveryId);

	void addClickTrackerIntoCache(ClickTracker clickTracker);

	ClickTracker queryClickTrackerFromCache(String clickId);

}
