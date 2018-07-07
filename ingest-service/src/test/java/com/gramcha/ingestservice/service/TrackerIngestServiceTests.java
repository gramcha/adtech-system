/**
 * @author gramcha
 * 07-Jul-2018 11:35:57 PM
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

import com.gramcha.ingestservice.models.AdClickTrackerRequest;
import com.gramcha.ingestservice.models.AdDeliveryTrackerRequest;
import com.gramcha.ingestservice.models.AdInstallTrackerRequest;
import com.gramcha.ingestservice.repos.AdClickTrackerRepository;
import com.gramcha.ingestservice.repos.AdDeliveryTrackerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackerIngestServiceTests {
	private static final String CLICK_ID = "fff54b83-49ff-476f-8bfb-2ec22b252c32";
	private static final String DELIVERY_ID = "244cf0db-ba28-4c5f-8c9c-2bf11ee42988";
	@Autowired
	TrackerIngestService ingestService;
	@Autowired
	private AdDeliveryTrackerRepository deliveryRepo;
	@Autowired
	private AdClickTrackerRepository clickRepo;

	@After
	public void tearDown() {
		deliveryRepo.delete(DELIVERY_ID);
		clickRepo.delete(CLICK_ID);
	}

	private AdDeliveryTrackerRequest createDeliveryTrackerRequest() {
		AdDeliveryTrackerRequest deliveryRequest = new AdDeliveryTrackerRequest();
		deliveryRequest.setAdvertisementId(4483);
		deliveryRequest.setBrowser("Chrome");
		deliveryRequest.setDeliveryId(DELIVERY_ID);
		deliveryRequest.setOs("iOS");
		deliveryRequest.setSite("http://super-dooper-news.com");
		deliveryRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:23").getTime());
		return deliveryRequest;
	}
	
	private AdClickTrackerRequest createClickTrackerRequest() {
		AdClickTrackerRequest clickTrackerRequest = new AdClickTrackerRequest();
		clickTrackerRequest.setDeliveryId(DELIVERY_ID);
		clickTrackerRequest.setClickId(CLICK_ID);
		clickTrackerRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:34").getTime());
		return clickTrackerRequest;
	}
	
	private AdInstallTrackerRequest createInstallTrackerRequest() {
		AdInstallTrackerRequest installRequest = new AdInstallTrackerRequest();
		installRequest.setClickId(CLICK_ID);
		installRequest.setInstallId("144cf0db-ba28-4c5f-8c9c-2bf11ee42988");
		installRequest.setTime(javax.xml.bind.DatatypeConverter.parseDateTime("2018-01-07T18:32:34").getTime());
		return installRequest;
	}
	
	@Test
	public void whenDeliveryTrackerRequestReceivedThenThatWillBeAccepted() throws Exception {
		AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
		Assert.assertEquals(true, ingestService.ingestDeliveryTracker(deliveryRequest));
	}

	@Test
	public void whenClickTrackerRequestReceivedWithValidDeliveryIdThenThatWillBeAccepted() throws Exception {
		AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
		ingestService.ingestDeliveryTracker(deliveryRequest);

		AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
		Assert.assertEquals(true, ingestService.ingestClickTracker(clickTrackerRequest));
	}

	
	@Test
	public void whenClickTrackerRequestReceivedWithInValidDeliveryIdThenThatWillNotBeAccepted() throws Exception {
		AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
		Assert.assertEquals(false, ingestService.ingestClickTracker(clickTrackerRequest));
	}
	
	@Test
	public void whenInstallTrackerRequestReceivedWithValidClickIdThenThatWillBeAccepted() throws Exception {
		AdDeliveryTrackerRequest deliveryRequest = createDeliveryTrackerRequest();
		ingestService.ingestDeliveryTracker(deliveryRequest);

		AdClickTrackerRequest clickTrackerRequest = createClickTrackerRequest();
		ingestService.ingestClickTracker(clickTrackerRequest);
		
		AdInstallTrackerRequest installRequest = createInstallTrackerRequest();
		Assert.assertEquals(true, ingestService.ingestInstallTracker(installRequest));
	}

	@Test
	public void whenInstallTrackerRequestReceivedWithInValidClickIdThenThatWillNotBeAccepted() throws Exception {
		AdInstallTrackerRequest installRequest = createInstallTrackerRequest();
		Assert.assertEquals(false, ingestService.ingestInstallTracker(installRequest));
	}
}
