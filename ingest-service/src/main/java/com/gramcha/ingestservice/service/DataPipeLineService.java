/**
 * @author gramcha
 * 08-Jul-2018 4:00:08 PM
 * 
 */
package com.gramcha.ingestservice.service;

import com.gramcha.ingestservice.entities.ClickTracker;
import com.gramcha.ingestservice.entities.DeliveryTracker;
import com.gramcha.ingestservice.entities.InstallTracker;

public interface DataPipeLineService {
	void send(DeliveryTracker tracker);

	void send(ClickTracker tracker);

	void send(InstallTracker tracker);
}
