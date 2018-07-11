package com.gramcha.queryservice.controller;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.queryservice.QueryServiceApplication;
import com.gramcha.queryservice.repos.ClickTrackerRepository;
import com.gramcha.queryservice.repos.DeliveryTrackerRepository;
import com.gramcha.queryservice.repos.InstallTrackerRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by gramcha on 11/07/18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(QueryServiceApplication.class)
public class StatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DeliveryTrackerRepository deliveryTrackerRepository;
    @Autowired
    ClickTrackerRepository clickTrackerRepository;
    @Autowired
    InstallTrackerRepository installTrackerRepository;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private static DeliveryTracker dTracker;
    private static ClickTracker cTracker;
    private static InstallTracker iTracker;

    @Before
    public void setup(){
        long advID = 4483;
        String deliveryId = UUID.randomUUID().toString();
        String browser = "safari";
        String os = "ios";
        String site = "http://super-dooper-news.com/";
        String clickId = UUID.randomUUID().toString();
        String installId = UUID.randomUUID().toString();
        dTracker = new DeliveryTracker(advID, deliveryId, new Date(), browser, os, site);
        System.out.println("dtracker - " + dTracker);
        deliveryTrackerRepository.save(dTracker);
        cTracker = new ClickTracker(advID, deliveryId, browser, os, site, clickId, new Date());
        clickTrackerRepository.save(cTracker);
        iTracker = new InstallTracker(advID, deliveryId, browser, os, site, installId, clickId,
                new Date());
        installTrackerRepository.save(iTracker);
    }
    @After
    public void tearDown(){
        if(dTracker !=null){
            deliveryTrackerRepository.delete(dTracker);
        }
        if(cTracker!=null){
            clickTrackerRepository.delete(cTracker);
        }
        if(iTracker!=null){
            installTrackerRepository.delete(iTracker);
        }
    }
    @Test
    public void testSimpleStatisticsWithoutStartAndEndTime() throws Exception {
        this.mockMvc.perform(get("/ads/statistics")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testSimpleStatisticsWithoutEndTime() throws Exception {
        this.mockMvc.perform(get("/ads/statistics?start=2010-01-07T14:30:00+0000")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testSimpleStatisticsWithEmptyEndTime() throws Exception {
        this.mockMvc.perform(get("/ads/statistics?start=2010-01-07T14:30:00+0000&end=")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testSimpleStatisticsWitStartAndEndTime() throws Exception {
        this.mockMvc.perform(get("/ads/statistics")
                .param("start","2011-01-07T14:30:00")
                .param("end","2019-02-07T18:20:00")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }
}
