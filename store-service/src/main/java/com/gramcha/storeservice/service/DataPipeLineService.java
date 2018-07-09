/**
 * @author gramcha
 * 08-Jul-2018 4:00:08 PM
 * 
 */
package com.gramcha.storeservice.service;

import org.springframework.kafka.support.Acknowledgment;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;

public interface DataPipeLineService {
	void receive(DeliveryTracker tracker, Acknowledgment acknowledgment);

	void receive(ClickTracker tracker, Acknowledgment acknowledgment);

	void receive(InstallTracker tracker, Acknowledgment acknowledgment);
}
