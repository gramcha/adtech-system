/**
 * @author gramcha
 * 08-Jul-2018 1:07:52 AM
 * 
 */
package com.gramcha.ingestservice.repos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gramcha.ingestservice.entities.DeliveryTracker;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdDeliveryTrackerRepositoryTests {
	@Autowired
	private AdDeliveryTrackerRepository deliveryRepo;
	private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";

	@After
	public void tearDown() {
		deliveryRepo.delete(DELIVERY_ID);
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
	
	@Test
	public void whenEntitySavedByRepoThenItIsSavedIntoSystem() throws Exception{
		DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
		deliveryRepo.save(deliveryrackerEntity);
		Assert.assertEquals(deliveryrackerEntity, deliveryRepo.findByDeliveryId(DELIVERY_ID));
	}
	
	@Test
	public void whenRepoQueriedBySavedClickedClickIdThenItReturnsCorrespondingEntity() throws Exception{
		DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
		deliveryRepo.save(deliveryrackerEntity);
		Assert.assertEquals(deliveryrackerEntity, deliveryRepo.findByDeliveryId(DELIVERY_ID));
	}
	
	@Test
	public void whenRepoQueriedByUnSavedClickedClickIdThenItReturnsNull() throws Exception{
		DeliveryTracker deliveryrackerEntity = createDeliverTrackerEntity();
		deliveryRepo.save(deliveryrackerEntity);
		Assert.assertNull(deliveryRepo.findByDeliveryId(DELIVERY_ID+"unsaved"));
	}
}
