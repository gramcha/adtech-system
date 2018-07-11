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
class AdTrackerQueryService implements QueryServer {
    public static final String FIELDS = "fields"
    public static final String STATS = "stats"
    public static final String INTERVAL = "interval"
    public static final String DATA = "data"
    public static final String COLUMN_TIME = "time"
    public static final String COUNT = "count"
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
        Aggregation agg = constructAggregator(sDate, eDate, groupBy)

        List<Object> deliveryResult = getAggregatedResultForDelivery(agg);
        List<Object> clickResult = getAggregatedResultForClick(agg);
        List<Object> installResult = getAggregatedResultForInstall(agg);

        HashMap<Object, Object> mergedResult =  mergeQueryResults(deliveryResult, clickResult, installResult, groupBy);
        List data = consurctResultData(mergedResult)

        LinkedHashMap resultObj = getResultObject(start, end, data)
        return resultObj
    }

    private LinkedHashMap getResultObject(String start, String end, List data) {
        def resultObj = [:];
        LinkedHashMap interval = createIntervalObject(start, end);
        resultObj[INTERVAL] = interval;
        resultObj[DATA] = data;
        return resultObj
    }

    private List consurctResultData(HashMap<Object, Object> mergedResult) {
        def data = [];
        for (Map.Entry<Object, Object> entry : mergedResult.entrySet()) {
            def key = entry.getKey();
            def value = entry.getValue();
            def dataItem = [:];
            dataItem[FIELDS] = key[FIELDS];
            dataItem[STATS] = value;
            data.add(dataItem);
        }
        return data
    }

    private Aggregation constructAggregator(Date sDate, Date eDate, String[] groupBy) {
        Aggregation agg = newAggregation(
                match(Criteria.where(COLUMN_TIME).gt(sDate).lt(eDate)),
                group(groupBy).count().as(COUNT)
        );
        return agg;
    }

    private List<Object> getAggregatedResultForInstall(Aggregation agg) {
        AggregationResults<Object> installGroupResults = mongoTemplate.aggregate(
                agg, InstallTracker.class, Object.class);
        List<Object> installResult = installGroupResults.getMappedResults();
        return installResult;
    }

    private List<Object> getAggregatedResultForClick(Aggregation agg) {
        AggregationResults<Object> clickGroupResults = mongoTemplate.aggregate(
                agg, ClickTracker.class, Object.class);
        List<Object> clickResult = clickGroupResults.getMappedResults();
        return clickResult;
    }

    private List<Object> getAggregatedResultForDelivery(Aggregation agg) {
        AggregationResults<Object> deliveryGroupResults = mongoTemplate.aggregate(
                agg, DeliveryTracker.class, Object.class);
        List<Object> deliveryResult = deliveryGroupResults.getMappedResults();
        return deliveryResult;
    }

    private HashMap<Object, Object> mergeQueryResults(List<Object> deliveryResult, List<Object> clickResult, List<Object> installResult, String[] groupBy) {
        HashMap<Object, Object> mapAccumulator = new HashMap<>();
        accumulateDeliveryResults(deliveryResult, groupBy, mapAccumulator);
        accumulateClickResults(clickResult, groupBy, mapAccumulator);
        accumulateInstallResults(installResult, groupBy, mapAccumulator);
        return mapAccumulator;
    }

    private void accumulate(List<Object> queryResult, String[] groupBy, HashMap<Object, Object> mapAccumulator,String statisticsType) {
        queryResult.each { install ->
            def filterObj = [:];
            def statsKey = [:];
            filterObj[FIELDS] = statsKey;
            install.each { k, v ->
                if (k == '_id' && groupBy.length == 1)
                    statsKey[groupBy[0]] = v;
                else if (k != 'count')
                    statsKey[k] = v;
            }
            if (!mapAccumulator.containsKey(filterObj)) {
                def statsvalue = [:]
                statsvalue[statisticsType] = install['count'];
                mapAccumulator.put(filterObj, statsvalue)
            } else {
                def statsvalue = mapAccumulator.get(filterObj);
                statsvalue[statisticsType] = install['count'];
                mapAccumulator.put(filterObj, statsvalue);
            }
        }
    }
    private void accumulateInstallResults(List<Object> installResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(installResult, groupBy, mapAccumulator,"installs");
    }

    private void accumulateClickResults(List<Object> clickResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(clickResult, groupBy, mapAccumulator,"clicks");
    }

    private void accumulateDeliveryResults(List<Object> deliveryResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(deliveryResult, groupBy, mapAccumulator,"deliveries");
    }

    private LinkedHashMap getSimpleStatistics(String start, String end) {
        Date sDate = DatatypeConverter.parseDateTime(start).getTime();
        Date eDate = DatatypeConverter.parseDateTime(end).getTime();
        List<DeliveryTracker> dResult = dRepo.findByTimeBetween(sDate, eDate);
        List<ClickTracker> cResult = cRepo.findByTimeBetween(sDate, eDate);
        List<InstallTracker> iResult = iRepo.findByTimeBetween(sDate, eDate);
        return getAggregatedSimpleStatistics(dResult, cResult, iResult,start,end)
    }

    private LinkedHashMap getAggregatedSimpleStatistics(List<DeliveryTracker> dResult, List<ClickTracker> cResult, List<InstallTracker> iResult,String start, String end) {
        LinkedHashMap interval = createIntervalObject(start,end);

        def stats = [:];
        stats["deliveries"] = dResult.size();
        stats["clicks"] = cResult.size();
        stats["installs"] = iResult.size();

        def resultObj = [:];
        resultObj[INTERVAL] = interval;
        resultObj[STATS] = stats;
        return resultObj
    }

    private LinkedHashMap createIntervalObject(String start, String end) {
        def interval = [:];
        interval["start"] = start;
        interval["end"] = end;
        return interval;
    }
}
