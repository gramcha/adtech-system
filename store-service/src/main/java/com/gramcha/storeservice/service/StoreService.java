/**
 * @author gramcha
 * 09-Jul-2018 10:00:36 PM
 * 
 */
package com.gramcha.storeservice.service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;

public interface StoreService {
	void saveTracker(DeliveryTracker tracker);
	void saveTracker(ClickTracker tracker);
	void saveTracker(InstallTracker tracker);
}
