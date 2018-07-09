/**
 * @author gramcha
 * 08-Jul-2018 1:00:00 AM
 * 
 */
package com.gramcha.ingestservice.repos;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gramcha.entities.ClickTracker;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdClickTrackerRepositoryTests {
	@Autowired
	private AdClickTrackerRepository clickRepo;
	private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
	private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";
	
	@After
	public void tearDown() {
		clickRepo.delete(CLICK_ID);
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
	public void whenEntitySavedByRepoThenItIsSavedIntoSystem() throws Exception{
		ClickTracker clickTrackerEntity = createClickrTrackerEntity();
		clickRepo.save(clickTrackerEntity);
		Assert.assertEquals(clickTrackerEntity, clickRepo.findByClickId(CLICK_ID));
	}
	
	@Test
	public void whenRepoQueriedBySavedClickedClickIdThenItReturnsCorrespondingEntity() throws Exception{
		ClickTracker clickTrackerEntity = createClickrTrackerEntity();
		clickRepo.save(clickTrackerEntity);
		Assert.assertEquals(clickTrackerEntity, clickRepo.findByClickId(CLICK_ID));
	}
	
	@Test
	public void whenRepoQueriedByUnSavedClickedClickIdThenItReturnsNull() throws Exception{
		ClickTracker clickTrackerEntity = createClickrTrackerEntity();
		clickRepo.save(clickTrackerEntity);
		Assert.assertNull(clickRepo.findByClickId(CLICK_ID+"unsaved"));
	}
}
