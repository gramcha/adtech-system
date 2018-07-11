package com.gramcha.queryservice.service;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.queryservice.repos.ClickTrackerRepository;
import com.gramcha.queryservice.repos.DeliveryTrackerRepository;
import com.gramcha.queryservice.repos.InstallTrackerRepository;
import org.junit.After
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * Created by gramcha on 11/07/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdTrackerQueryServiceTests {
    @Autowired
    DeliveryTrackerRepository deliveryTrackerRepository;
    @Autowired
    ClickTrackerRepository clickTrackerRepository;
    @Autowired
    InstallTrackerRepository installTrackerRepository;

    @Autowired
    AdTrackerQueryService queryService;

    private static DeliveryTracker dTracker;
    private static ClickTracker cTracker;
    private static InstallTracker iTracker;

    @After
    public void tearDown() {
        deliveryTrackerRepository.deleteAll();
        clickTrackerRepository.deleteAll();
        installTrackerRepository.deleteAll();
    }

    @Test
    public void whenSimipleStatisticsQuiredWithStartTimeAndEndTimeThenCorrespondingResultReturned() {
        Date deliveryDate = new Date();
        Date clickDate = deliveryDate;
        Date installDate = deliveryDate;
        createTrackerSet( deliveryDate, clickDate, installDate);
        String groupBy=[];
        def result = queryService.getStatistics("2017-01-07T14:30:00","2020-01-07T14:30:00",null);
        Assert.assertEquals(1,result.stats.deliveries);
        Assert.assertEquals(1,result.stats.clicks);
        Assert.assertEquals(1,result.stats.installs);
    }
    @Test
    public void whenSimipleStatisticsQuriedForZeroClicksThenCorrespondingResultReturned() {
        Date deliveryDate = new Date();
        Date clickDate = deliveryDate;
        Date installDate = deliveryDate;
        createTrackerSet( deliveryDate, null, null);
        String groupBy=[];
        def result = queryService.getStatistics("2017-01-07T14:30:00","2020-01-07T14:30:00",null);
        Assert.assertEquals(1,result.stats.deliveries);
        Assert.assertEquals(0,result.stats.clicks);
        Assert.assertEquals(0,result.stats.installs);
    }
    @Test
    public void whenSimipleStatisticsQuriedForZeroInstallsThenCorrespondingResultReturned() {
        Date deliveryDate = new Date();
        Date clickDate = deliveryDate;
        Date installDate = deliveryDate;
        createTrackerSet( deliveryDate, clickDate, null);
        String groupBy=[];
        def result = queryService.getStatistics("2017-01-07T14:30:00","2020-01-07T14:30:00",null);
        Assert.assertEquals(1,result.stats.deliveries);
        Assert.assertEquals(1,result.stats.clicks);
        Assert.assertEquals(0,result.stats.installs);
    }

    private void createTrackerSet(Date deliveryDate, Date clickDate, Date installDate) {
        long advID = 4483;
        String deliveryId = UUID.randomUUID().toString();
        String browser = "safari";
        String os = "ios";
        String site = "http://super-dooper-news.com/";
        String clickId = UUID.randomUUID().toString();
        String installId = UUID.randomUUID().toString();
        dTracker = new DeliveryTracker(advID, deliveryId, deliveryDate, browser, os, site);
        System.out.println("dtracker - " + dTracker);
        deliveryTrackerRepository.save(dTracker);
        cTracker = new ClickTracker(advID, deliveryId, browser, os, site, clickId, clickDate);
        clickTrackerRepository.save(cTracker);
        if (installDate!=null){
            iTracker = new InstallTracker(advID, deliveryId, browser, os, site, installId, clickId,
                    installDate);
            installTrackerRepository.save(iTracker);
        }

    }
    @Test
    public void whenGroupByStatisticsQuiredWithStartTimeAndEndTimeThenCorrespondingResultReturned() {
        Date deliveryDate = new Date();
        Date clickDate = deliveryDate;
        Date installDate = deliveryDate;
        createTrackerSet( deliveryDate, clickDate, installDate);
        String[] groupBy = ["browser"] as String[]
        def result = queryService.getStatistics("2017-01-07T14:30:00","2020-01-07T14:30:00",groupBy);
        Assert.assertEquals("safari",result.data[0]["fields"].browser);
        Assert.assertEquals(1,result.data[0]["stats"].deliveries);
        Assert.assertEquals(1,result.data[0]["stats"].clicks);
        Assert.assertEquals(1,result.data[0]["stats"].installs);
    }
    @Test
    public void whenGroupByStatisticsQuiredWithMultipleGroupByOptionsThenCorrespondingResultReturned() {
        Date deliveryDate = new Date();
        Date clickDate = deliveryDate;
        Date installDate = deliveryDate;
        createTrackerSet( deliveryDate, clickDate, installDate);
        String[] groupBy = ["browser","site"] as String[]
        def result = queryService.getStatistics("2017-01-07T14:30:00","2020-01-07T14:30:00",groupBy);
        Assert.assertEquals("safari",result.data[0]["fields"].browser);
        Assert.assertEquals("http://super-dooper-news.com/",result.data[0]["fields"].site);
        Assert.assertEquals(1,result.data[0]["stats"].deliveries);
        Assert.assertEquals(1,result.data[0]["stats"].clicks);
        Assert.assertEquals(1,result.data[0]["stats"].installs);
    }
}
