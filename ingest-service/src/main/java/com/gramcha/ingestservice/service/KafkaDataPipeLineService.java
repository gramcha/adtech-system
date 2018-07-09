/**
 * @author gramcha
 * 08-Jul-2018 4:05:09 PM
 * 
 */
package com.gramcha.ingestservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;

@Service
public class KafkaDataPipeLineService implements DataPipeLineService {
	private final static String DELIVERY_TOPIC = "delivery";
	private final static String CLICK_TOPIC = "click";
	private final static String INSTALL_TOPIC = "install";
	@Autowired
	private KafkaTemplate<String, DeliveryTracker> deliveryProducer;

	@Autowired
	private KafkaTemplate<String, ClickTracker> clickProducer;

	@Autowired
	private KafkaTemplate<String, InstallTracker> installProducer;

	@Override
	public void send(DeliveryTracker tracker) {
		deliveryProducer.send(DELIVERY_TOPIC, tracker.getDeliveryId(), tracker);
	}

	@Override
	public void send(ClickTracker tracker) {
		clickProducer.send(CLICK_TOPIC, tracker.getClickId(), tracker);
	}

	@Override
	public void send(InstallTracker tracker) {
		installProducer.send(INSTALL_TOPIC, tracker.getInstallId(), tracker);
	}
}
