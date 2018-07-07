/**
 * @author gramcha
 * 07-Jul-2018 10:26:56 PM
 * 
 */
package com.gramcha.ingestservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gramcha.ingestservice.IngestServiceApplication;
import com.gramcha.ingestservice.repos.AdClickTrackerRepository;
import com.gramcha.ingestservice.repos.AdDeliveryTrackerRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(IngestServiceApplication.class)
public class AdTrackerControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AdDeliveryTrackerRepository deliveryRepo;
	@Autowired
	private AdClickTrackerRepository clickRepo;

	private static final String DELIVERY_ID_ESC_STR = "\"244cf0db-ba28-4c5f-8c9c-2bf11ee42988\"";
	private static final String CLICK_ID_ESC_STR = "\"fff54b83-49ff-476f-8bfb-2ec22b252c32\"";
	private static final String Install_ID_ESC_STR = "\"144cf0db-ba28-4c5f-8c9c-2bf11ee42988\"";

	@After
	public void setup() throws Exception {
		deliveryRepo.delete("244cf0db-ba28-4c5f-8c9c-2bf11ee42988");
		clickRepo.delete("fff54b83-49ff-476f-8bfb-2ec22b252c32");
	}

	private String getDeliveryTrackerJsonString() {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"advertisementId\": 4483");
		json.append(",");
		json.append("\"deliveryId\": " + DELIVERY_ID_ESC_STR);
		json.append(",");
		json.append("\"time\": \"2018-01-07T18:32:23.602300+0000\"");
		json.append(",");
		json.append("\"browser\": \"Chrome\"");
		json.append(",");
		json.append("\"os\": \"iOS\"");
		json.append(",");
		json.append("\"site\": \"http://super-dooper-news.com\"");
		json.append("}");
		return json.toString();
	}

	private String getClickTrackerJsonString() {
		StringBuilder json;
		json = new StringBuilder();
		json.append("{");
		json.append("\"deliveryId\": " + DELIVERY_ID_ESC_STR);
		json.append(",");
		json.append("\"clickId\": " + CLICK_ID_ESC_STR);
		json.append(",");
		json.append("\"time\": \"2018-01-07T18:32:34.201100+0000\"");
		json.append("}");
		return json.toString();
	}

	private String getInstallTrackerJsonString() {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"installId\": " + Install_ID_ESC_STR);
		json.append(",");
		json.append("\"clickId\": " + CLICK_ID_ESC_STR);
		json.append(",");
		json.append("\"time\": \"2018-01-07T18:32:34.201100+0000\"");
		json.append("}");
		return json.toString();
	}

	@Test
	public void testDeliveryTracker() throws Exception {
		String json = getDeliveryTrackerJsonString();
		mockMvc.perform(post("/ads/delivery").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));
	}

	
	@Test
	public void testClickTrackerForValidDeliveryId() throws Exception {
		String json = getDeliveryTrackerJsonString();
		mockMvc.perform(post("/ads/delivery").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));

		json = getClickTrackerJsonString();
		mockMvc.perform(post("/ads/click").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));
	}

	@Test
	public void testClickTrackerForUnknownDeliveryId() throws Exception {
		String json = getClickTrackerJsonString();
		mockMvc.perform(post("/ads/click").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isNoContent())
				.andExpect(content().bytes(new byte[0]));
	}

	@Test
	public void testInstallTrackerForValidClickId() throws Exception {
		String json = getDeliveryTrackerJsonString();
		mockMvc.perform(post("/ads/delivery").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));

		json = getClickTrackerJsonString();
		mockMvc.perform(post("/ads/click").contentType("application/json;charset=UTF-8").content(json)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));
		
		json = getInstallTrackerJsonString();
		mockMvc.perform(post("/ads/install").contentType("application/json;charset=UTF-8").content(json.toString())
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));

	}

	@Test
	public void testClickTrackerForUnknownClickId() throws Exception {
		String json = getInstallTrackerJsonString();
		mockMvc.perform(post("/ads/install").contentType("application/json;charset=UTF-8").content(json.toString())
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isNoContent())
				.andExpect(content().bytes(new byte[0]));
	}
}
