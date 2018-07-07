/**
 * @author gramcha
 * 08-Jul-2018 12:28:52 AM
 * 
 */
package com.gramcha.ingestservice.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gramcha.ingestservice.entities.ClickTracker;
import com.gramcha.ingestservice.entities.DeliveryTracker;
import com.gramcha.ingestservice.repos.AdClickTrackerRepository;
import com.gramcha.ingestservice.repos.AdDeliveryTrackerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheServiceTests {

	@Autowired
	CacheService dbCache;
	@Autowired
	private AdDeliveryTrackerRepository deliveryRepo;
	@Autowired
	private AdClickTrackerRepository clickRepo;
	private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
	private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";

	@After
	public void tearDown() {
		deliveryRepo.delete(DELIVERY_ID);
		clickRepo.delete(CLICK_ID);
	}

	private DeliveryTracker createDeliverTrackerEntity() {
		DeliveryTracker deliveryTrackerEntity = new DeliveryTracker();
		deliveryTrackerEntity.setAdvertisementId(4483);
		deliveryTrackerEntity.setDeliveryId(DELIVERY_ID);
		deliveryTrackerEntity.setBrowser("Chrome");
		deliveryTrackerEntity.setOs("iOS");
		deliveryTrackerEntity.setSite("http://super-dooper-news.com");
		deliveryTrackerEntity.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:23").getTime());
		return deliveryTrackerEntity;
	}
	private ClickTracker createClickrTrackerEntity() {
		ClickTracker clickTrackerEntity = new ClickTracker();
		clickTrackerEntity.setAdvertisementId(4483);
		clickTrackerEntity.setDeliveryId(DELIVERY_ID);
		clickTrackerEntity.setBrowser("Chrome");
		clickTrackerEntity.setOs("iOS");
		clickTrackerEntity.setSite("http://super-dooper-news.com");
		clickTrackerEntity.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:34:00").getTime());
		clickTrackerEntity.setClickId(CLICK_ID);
		return clickTrackerEntity;
	}
	
	@Test
	public void whenDeliveryTrackerEntityAddedIntoCacheThenItIsSavedCacheServer() throws Exception {
		DeliveryTracker deliveryTrackerEntity = createDeliverTrackerEntity();
		dbCache.addDeliveryTrackerIntoCache(deliveryTrackerEntity);
		DeliveryTracker queriedEntity = deliveryRepo.findByDeliveryId(DELIVERY_ID);
		Assert.assertEquals(deliveryTrackerEntity, queriedEntity);
	}

	@Test
	public void whenCacheQueriedWithValidDeliveryTrackerIdThenCorrespondingDeliverTrackerEntityReturned()
			throws Exception {
		DeliveryTracker deliveryTrackerEntity = createDeliverTrackerEntity();
		dbCache.addDeliveryTrackerIntoCache(deliveryTrackerEntity);
		DeliveryTracker queriedEntity = dbCache.queryDeliveryTrackerFromCache(DELIVERY_ID);
		Assert.assertEquals(deliveryTrackerEntity, queriedEntity);
	}

	@Test
	public void whenCacheQueriedWithDeliveryTrackerIdWhichIsNotCachedThenNulleturned() throws Exception {
		DeliveryTracker queriedEntity = dbCache.queryDeliveryTrackerFromCache(DELIVERY_ID);
		Assert.assertNull(queriedEntity);
	}
	
	@Test
	public void whenClickTrackerEntityAddedIntoCacheThenItIsSavedCacheServer() throws Exception {
		ClickTracker clickTrackerEntity = createClickrTrackerEntity();
		dbCache.addClickTrackerIntoCache(clickTrackerEntity);
		ClickTracker queriedEntity = clickRepo.findByClickId(CLICK_ID);
		Assert.assertEquals(clickTrackerEntity, queriedEntity);
	}
	
	@Test
	public void whenCacheQueriedWithClickTrackerIdWhichIsNotCachedThenNulleturned() throws Exception {
		ClickTracker queriedEntity = dbCache.queryClickTrackerFromCache(CLICK_ID);
		Assert.assertNull(queriedEntity);
	}
}
