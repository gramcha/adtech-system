/**
 * @author gramcha
 * 08-Jul-2018 4:00:08 PM
 * 
 */
package com.gramcha.ingestservice.service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;

public interface DataPipeLineService {
	void send(DeliveryTracker tracker);

	void send(ClickTracker tracker);

	void send(InstallTracker tracker);
}
