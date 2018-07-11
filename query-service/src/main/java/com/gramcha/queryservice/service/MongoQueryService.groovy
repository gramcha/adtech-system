package com.gramcha.queryservice.service

import com.gramcha.entities.ClickTracker
import com.gramcha.entities.DeliveryTracker
import com.gramcha.entities.InstallTracker
import com.gramcha.queryservice.repos.ClickTrackerRepository
import com.gramcha.queryservice.repos.DeliveryTrackerRepository
import com.gramcha.queryservice.repos.InstallTrackerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

import javax.xml.bind.DatatypeConverter

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation

/**
 * Created by gramcha on 11/07/18.
 */
@Service
class MongoQueryService implements QueryServer {
    @Autowired
    DeliveryTrackerRepository dRepo;

    @Autowired
    ClickTrackerRepository cRepo;

    @Autowired
    InstallTrackerRepository iRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    Object getStatistics(String start, String end, String[] groupBy) {
        start = start.split(" ")[0];
        end = end.split(" ")[0];


        if (groupBy == null || groupBy.length < 1) {
            //Statistics Type1 - Simple Statistics
            return getSimpleStatistics(start, end);
        }
        //Statistics Type2 - GroupBy Statistics
        return getGroupByStatistics(start, end, groupBy);
    }

    private Object getGroupByStatistics(String start, String end, String[] groupBy) {
        Date sDate = DatatypeConverter.parseDateTime(start).getTime();
        Date eDate = DatatypeConverter.parseDateTime(end).getTime();
        Aggregation agg = newAggregation(
                match(Criteria.where("time").gt(sDate).lt(eDate)),
                group(groupBy).count().as("count")
        );

        AggregationResults<Object> deliveryGroupResults = mongoTemplate.aggregate(
                agg, DeliveryTracker.class, Object.class);
        List<Object> deliveryResult = deliveryGroupResults.getMappedResults();

        AggregationResults<Object> clickGroupResults = mongoTemplate.aggregate(
                agg, ClickTracker.class, Object.class);
        List<Object> clickResult = clickGroupResults.getMappedResults();

        AggregationResults<Object> installGroupResults = mongoTemplate.aggregate(
                agg, InstallTracker.class, Object.class);
        List<Object> installResult = installGroupResults.getMappedResults();
//        return deliveryResult;
        HashMap<Object, Object> mergedResult =  mergeQueryResults(deliveryResult, clickResult, installResult, groupBy);
        def data = [];
        for(Map.Entry<Object, Object> entry : mergedResult.entrySet()) {
            def key = entry.getKey();
            def value = entry.getValue();
            def dataItem = [:];
            dataItem["fields"] = key["fields"];
            dataItem["stats"] = value;
            data.add(dataItem);
        }

        def resultObj = [:];
        LinkedHashMap interval = createIntervalObject(start,end);
        resultObj["interval"] = interval;
        resultObj["data"] = data;
        return resultObj
    }

    private HashMap<Object, Object> mergeQueryResults(List<Object> deliveryResult, List<Object> clickResult, List<Object> installResult, String[] groupBy) {
        HashMap<Object, Object> mapAccumulator = new HashMap<>();
        def groupByStats = [:]
        deliveryResult.each { delivery ->
            def filterObj = [:];
            def statsKey = [:];
            filterObj["fields"] = statsKey;
            delivery.each { k, v ->
                println(" item - k"+ k)
                println(" item - v"+ v)
                if (k == '_id' && groupBy.length==1){
                    statsKey[groupBy[0]] = v;
                } else if (k != 'count')
                    statsKey[k] = v;
            }
            if (mapAccumulator.containsKey(filterObj)==false) {
                println("---missing---")
                println(statsKey)
                def statsvalue = [:]
                statsvalue['deliveries']=delivery['count'];
                mapAccumulator.put(filterObj,statsvalue)
            } else {
                println("---found---")
                println(statsKey)
                def statsvalue = mapAccumulator.get(filterObj);
                statsvalue['deliveries']=delivery['count'];
                mapAccumulator.put(filterObj,statsvalue);
            }
        }
        clickResult.each { click ->
            def filterObj = [:];
            def statsKey = [:];
            filterObj["fields"] = statsKey;
            click.each { k, v ->
                println(" item - k"+ k)
                println(" item - v"+ v)
                if (k == '_id' && groupBy.length==1){
                    statsKey[groupBy[0]] = v;
                } else if (k != 'count')
                    statsKey[k] = v;
            }
            if (mapAccumulator.containsKey(filterObj)==false) {
                println("---missing---")
                println(statsKey)
                def statsvalue = [:]
                statsvalue['clicks']=click['count'];
                mapAccumulator.put(filterObj,statsvalue)
            } else {
                println("---found---")
                println(statsKey)
                def statsvalue = mapAccumulator.get(filterObj);
                statsvalue['clicks']=click['count'];
                mapAccumulator.put(filterObj,statsvalue);
            }
        }
        installResult.each { install ->
            def filterObj = [:];
            def statsKey = [:];
            filterObj["fields"] = statsKey;
            install.each { k, v ->
                println(" item - k"+ k)
                println(" item - v"+ v)
                if (k == '_id' && groupBy.length==1){
                    statsKey[groupBy[0]] = v;
                } else if (k != 'count')
                    statsKey[k] = v;
            }
            if (mapAccumulator.containsKey(filterObj)==false) {
                println("---missing---")
                println(statsKey)
                def statsvalue = [:]
                statsvalue['installs']=install['count'];
                mapAccumulator.put(filterObj,statsvalue)
            } else {
                println("---found---")
                println(statsKey)
                def statsvalue = mapAccumulator.get(filterObj);
                statsvalue['installs']=install['count'];
                mapAccumulator.put(filterObj,statsvalue);
            }
        }
        System.out.println(mapAccumulator.toString());
        return mapAccumulator;
    }

    private LinkedHashMap getSimpleStatistics(String start, String end) {
        Date sDate = DatatypeConverter.parseDateTime(start).getTime();
        Date eDate = DatatypeConverter.parseDateTime(end).getTime();
        List<DeliveryTracker> dResult = dRepo.findByTimeBetween(sDate, eDate);
        List<ClickTracker> cResult = cRepo.findByTimeBetween(sDate, eDate);
        List<InstallTracker> iResult = cRepo.findByTimeBetween(sDate, eDate);
        return getAggregatedSimpleStatistics(dResult, cResult, iResult,start,end)
    }

    private LinkedHashMap getAggregatedSimpleStatistics(List<DeliveryTracker> dResult, List<ClickTracker> cResult, List<InstallTracker> iResult,String start, String end) {
        LinkedHashMap interval = createIntervalObject(start,end);

        def stats = [:];
        stats["deliveries"] = dResult.size();
        stats["clicks"] = cResult.size();
        stats["installs"] = iResult.size();

        def resultObj = [:];
        resultObj["interval"] = interval;
        resultObj["stats"] = stats;
        return resultObj
    }

    private LinkedHashMap createIntervalObject(String start, String end) {
        def interval = [:];
        interval["start"] = start;
        interval["end"] = end;
        return interval;
    }
}
