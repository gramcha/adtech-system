/**
 * @author gramcha
 * 07-Jul-2018 4:17:19 PM
 * 
 */
package com.gramcha.ingestservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.ingestservice.models.AdClickTrackerRequest;
import com.gramcha.ingestservice.models.AdDeliveryTrackerRequest;
import com.gramcha.ingestservice.models.AdInstallTrackerRequest;

@Service
public class TrackerIngestServiceImpl implements TrackerIngestService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private CacheService dbCache;
	@Autowired
	private DataPipeLineService dataPipeline;

	@Override
	public boolean ingestDeliveryTracker(AdDeliveryTrackerRequest deliveryPayload) {
		return checkAndStoreIntoCache(deliveryPayload);
	}

	private boolean checkAndStoreIntoCache(AdDeliveryTrackerRequest deliveryPayload) {
		DeliveryTracker deliveryTrackerEntity = fillDeliveryEnitity(deliveryPayload);
		dbCache.addDeliveryTrackerIntoCache(deliveryTrackerEntity);
		dataPipeline.send(deliveryTrackerEntity);
		return true;
	}

	private DeliveryTracker fillDeliveryEnitity(AdDeliveryTrackerRequest deliveryPayload) {
		DeliveryTracker deliveryTrackerEntity = new DeliveryTracker();
		deliveryTrackerEntity.setAdvertisementId(deliveryPayload.getAdvertisementId());
		deliveryTrackerEntity.setBrowser(deliveryPayload.getBrowser());
		deliveryTrackerEntity.setDeliveryId(deliveryPayload.getDeliveryId());
		deliveryTrackerEntity.setOs(deliveryPayload.getOs());
		deliveryTrackerEntity.setSite(deliveryPayload.getSite());
		deliveryTrackerEntity.setTime(deliveryPayload.getTime());
		return deliveryTrackerEntity;
	}

	@Override
	public boolean ingestClickTracker(AdClickTrackerRequest clickPayload) {
		return checkAndStoreIntoCache(clickPayload);
	}

	private boolean checkAndStoreIntoCache(AdClickTrackerRequest clickPayload) {
		DeliveryTracker deliveryTracker = getDeliveryTracker(clickPayload.getDeliveryId());
		if (deliveryTracker != null) {
			ClickTracker clickTrackerEntity = fillClickTrackerEntity(clickPayload, deliveryTracker);
			dbCache.addClickTrackerIntoCache(clickTrackerEntity);
			dataPipeline.send(clickTrackerEntity);
			return true;
		} else {
			LOG.error("Delivery Id " + clickPayload.getDeliveryId() + " is missing for given ClickTracker id "
					+ clickPayload.getClickId());
			return false;
		}
	}

	private ClickTracker fillClickTrackerEntity(AdClickTrackerRequest clickPayload, DeliveryTracker deliveryTracker) {
		ClickTracker clickTrackerEntity = new ClickTracker();
		clickTrackerEntity.setAdvertisementId(deliveryTracker.getAdvertisementId());
		clickTrackerEntity.setBrowser(deliveryTracker.getBrowser());
		clickTrackerEntity.setOs(deliveryTracker.getOs());
		clickTrackerEntity.setSite(deliveryTracker.getSite());
		clickTrackerEntity.setClickId(clickPayload.getClickId());
		clickTrackerEntity.setDeliveryId(clickPayload.getDeliveryId());
		clickTrackerEntity.setTime(clickPayload.getTime());
		return clickTrackerEntity;
	}

	private DeliveryTracker getDeliveryTracker(String deliveryId) {
		return dbCache.queryDeliveryTrackerFromCache(deliveryId);
	}

	@Override
	public boolean ingestInstallTracker(AdInstallTrackerRequest installPayload) {
		return checkAndStoreIntoCache(installPayload);
	}

	private boolean checkAndStoreIntoCache(AdInstallTrackerRequest installPayload) {
		ClickTracker clickTracker = getClickTracker(installPayload.getClickId());
		if (clickTracker != null) {
			InstallTracker installEntity = new InstallTracker();
			installEntity.setAdvertisementId(clickTracker.getAdvertisementId());
			installEntity.setBrowser(clickTracker.getBrowser());
			installEntity.setOs(clickTracker.getOs());
			installEntity.setSite(clickTracker.getSite());
			installEntity.setClickId(clickTracker.getClickId());
			installEntity.setDeliveryId(clickTracker.getDeliveryId());
			installEntity.setInstallId(installPayload.getInstallId());
			installEntity.setTime(installPayload.getTime());
			dataPipeline.send(installEntity);
			return true;
		}else {
			LOG.error("Click Id " + installPayload.getClickId() + " is missing for given InstallTracker id "
					+ installPayload.getInstallId());
			return false;
		}
	}

	private ClickTracker getClickTracker(String clickId) {
		ClickTracker result = dbCache.queryClickTrackerFromCache(clickId);
		return result;
	}
}
