/**
 * @author gramcha
 * 10-Jul-2018 2:48:49 PM
 */
package com.gramcha.queryservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.ClickTracker;
import com.gramcha.entities.DeliveryTracker;
import com.gramcha.entities.InstallTracker;
import com.gramcha.queryservice.repos.ClickTrackerRepository;
import com.gramcha.queryservice.repos.DeliveryTrackerRepository;
import com.gramcha.queryservice.repos.InstallTrackerRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class QueryController {

    @Autowired
    DeliveryTrackerRepository dRepo;

    @Autowired
    ClickTrackerRepository cRepo;

    @Autowired
    InstallTrackerRepository iRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/dummy", method = RequestMethod.GET)
    public void dataCreation1() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            writeIntoDb();
        }
    }

    private void writeIntoDb() throws InterruptedException {
        System.out.println("-");
        int random = (int) (Math.random() * 50 + 1);
        List<String> browsers = Arrays.asList("chrome", "safari", "firefox");
        List<String> oss = Arrays.asList("ios", "ubuntu", "windows", "android");
        List<String> sites = Arrays.asList("google.com", "yahoo.com", "fb.com", "http://super-dooper-news.com");
        long advID = (int) (Math.random() * 9999 + 1);
        String deliveryId = UUID.randomUUID().toString();
        String browser = browsers.get(random % browsers.size());
        random = (int) (Math.random() * 50 + 1);
        String os = oss.get(random % oss.size());
        random = (int) (Math.random() * 50 + 1);
        String site = sites.get(random % sites.size());
        String clickId = UUID.randomUUID().toString();
        String installId = UUID.randomUUID().toString();
        DeliveryTracker tracker = new DeliveryTracker(advID, deliveryId, new Date(), browser, os, site);
        System.out.println("dtracker - " + tracker);
        dRepo.save(tracker);
        Thread.sleep(1000);
        ClickTracker cTracker = new ClickTracker(advID, deliveryId, browser, os, site, clickId, new Date());
        cRepo.save(cTracker);
        System.out.println("cTracker - " + cTracker);
        Thread.sleep(1000);
        InstallTracker iTracker = new InstallTracker(advID, deliveryId, browser, os, site, installId, clickId,
                new Date());
        iRepo.save(iTracker);
        System.out.println("iTracker - " + iTracker);
        Thread.sleep(1000);
    }

    //	@RequestMapping(value = "/ads/statistics", method = RequestMethod.GET)
//	public List<DeliveryTracker> getStatistics(@RequestParam(required = true, value = "start") String start,
//			@RequestParam(required = true, value = "end") String end) {
//		start = start.split(" ")[0];
//		end = end.split(" ")[0];
//		System.out.println(start);
//		System.out.println(end);
//		System.out.println(javax.xml.bind.DatatypeConverter.parseDateTime(start).getTime());
//		System.out.println(javax.xml.bind.DatatypeConverter.parseDateTime(end).getTime());
//		Date sdate = javax.xml.bind.DatatypeConverter.parseDateTime(start).getTime();
//		Date edate = javax.xml.bind.DatatypeConverter.parseDateTime(end).getTime();
//		List<DeliveryTracker> result = dRepo.findByTimeBetween(sdate, edate);
//		System.out.println(result);
//		return result;
//	}
    @RequestMapping(value = "/ping1", method = RequestMethod.GET)
    public List<DeliveryTracker> ping() throws InterruptedException {
        // for(int i=0;i<10;i++) {
        // dataCreation1();
        // }
        List<DeliveryTracker> resultList = null;
        Date start = new Date(2010, 01, 01, 00, 00);
        Date end = new Date(2019, 07, 01, 10, 00);

        MongoClient mongo = new MongoClient("mongo", 27017);
        DB db = mongo.getDB("adtech");
        DBCollection collection = db.getCollection("delivery");
        BasicDBObject query = new BasicDBObject();
        query.put("time", BasicDBObjectBuilder.start("$gt", start).get());
        DBCursor result = collection.find(query);
        while (result.hasNext()) {
            // DBObject item = result.next();
            System.out.println(result.next());
        }
        // Query query = new
        // Query().addCriteria(Criteria.where("time").lte(end).gt(start));
        // resultList = mongoTemplate.find(query, DeliveryTracker.class);
        // System.out.println(resultList);
        // resultList =mongoTemplate.findAll(DeliveryTracker.class);

        return resultList;

        // List<DeliveryTracker> resultList =
        // dRepo.findByTimePeroid(javax.xml.bind.DatatypeConverter.parseDateTime("2018-07-10T10:18:40").getTime(),
        // javax.xml.bind.DatatypeConverter.parseDateTime("2018-07-10T10:19:05").getTime());
        // resultList = dRepo.findByTimePeroid(start,end);
        // System.out.println(" -- findByTimePeroid --");
        // System.out.println(resultList);
        // resultList = dRepo.findByTimeBetween(start,end);
        // System.out.println(" -- findByTimeBetween --");
        // System.out.println(resultList);
        // return resultList.toString();
        // return "ping pong";
    }
}


