/**
 * @author gramcha
 * 07-Jul-2018 5:00:49 PM
 * 
 */
package com.gramcha.ingestservice.service;

import com.gramcha.ingestservice.models.AdClickTrackerRequest;
import com.gramcha.ingestservice.models.AdDeliveryTrackerRequest;
import com.gramcha.ingestservice.models.AdInstallTrackerRequest;

public interface TrackerIngestService {
	boolean ingestDeliveryTracker(AdDeliveryTrackerRequest deliveryPayload);
	boolean ingestClickTracker(AdClickTrackerRequest clickPayload);
	boolean ingestInstallTracker(AdInstallTrackerRequest installPayload);
}
