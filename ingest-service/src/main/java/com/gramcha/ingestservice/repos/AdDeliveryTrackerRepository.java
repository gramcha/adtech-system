/**
 * @author gramcha
 * 07-Jul-2018 4:09:24 PM
 * 
 */
package com.gramcha.ingestservice.repos;

import com.gramcha.ingestservice.entities.DeliveryTracker;

public interface AdDeliveryTrackerRepository {
	void save(DeliveryTracker deliveryTracker);
	DeliveryTracker findByDeliveryId(String deliveryId);
	void delete(String deliveryId);
}
