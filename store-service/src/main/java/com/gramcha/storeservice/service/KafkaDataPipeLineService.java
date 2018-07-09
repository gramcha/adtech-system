/**
 * @author gramcha
 * 08-Jul-2018 4:05:09 PM
 * 
 */
package com.gramcha.storeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;

@Service
public class KafkaDataPipeLineService implements DataPipeLineService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final static String DELIVERY_TOPIC = "delivery";
	private final static String CLICK_TOPIC = "click";
	private final static String INSTALL_TOPIC = "install";

	@Autowired
	StoreService store;
	
	@KafkaListener(topics = DELIVERY_TOPIC)
	@Override
	public void receive(DeliveryTracker tracker, Acknowledgment acknowledgment) {
		store.saveTracker(tracker);
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived delivery tracker " + tracker.getDeliveryId());
		acknowledgment.acknowledge();
	}

	@KafkaListener(topics = CLICK_TOPIC)
	@Override
	public void receive(ClickTracker tracker, Acknowledgment acknowledgment) {
		store.saveTracker(tracker);
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived click tracker " + tracker.getClickId());
		acknowledgment.acknowledge();
	}

	@KafkaListener(topics = INSTALL_TOPIC)
	@Override
	public void receive(InstallTracker tracker, Acknowledgment acknowledgment) {
		store.saveTracker(tracker);
		LOG.info("KAFKA consumer ....");
		LOG.info("Revceived install tracker " + tracker.getInstallId());
		acknowledgment.acknowledge();
	}
}
