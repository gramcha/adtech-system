package com.gramcha.queryservice.service

import com.gramcha.entities.ClickTracker
import com.gramcha.entities.DeliveryTracker
import com.gramcha.entities.InstallTracker
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
class GroupByQueryExecutorService {
    public static final String FIELDS = "fields"
    public static final String COLUMN_TIME = "time"
    public static final String COUNT = "count"
    public static final String ID = '_id'
    public static final String TYPE_INSTALLS = "installs"
    public static final String TYPE_CLICKS = "clicks"
    public static final String TYPE_DELIVERIES = "deliveries"
    @Autowired
    MongoTemplate mongoTemplate;
    public HashMap<Object, Object> execute(String start, String end, String[] groupBy){
        Date sDate = DatatypeConverter.parseDateTime(start).getTime();
        Date eDate = DatatypeConverter.parseDateTime(end).getTime();
        Aggregation agg = constructAggregator(sDate, eDate, groupBy)

        List<Object> deliveryResult = getAggregatedResultForDelivery(agg);
        List<Object> clickResult = getAggregatedResultForClick(agg);
        List<Object> installResult = getAggregatedResultForInstall(agg);
        return mergeQueryResults(deliveryResult, clickResult, installResult, groupBy);
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
        queryResult.each { item ->
            def filterObj = [:];
            def statsKey = [:];
            filterObj[FIELDS] = statsKey;
            item.each { k, v ->
                if (k == ID && groupBy.length == 1)
                    statsKey[groupBy[0]] = v;
                else if (k != COUNT)
                    statsKey[k] = v;
            }
            if (!mapAccumulator.containsKey(filterObj)) {
                def statsvalue = [:]
                statsvalue[statisticsType] = item[COUNT];
                mapAccumulator.put(filterObj, statsvalue)
            } else {
                def statsvalue = mapAccumulator.get(filterObj);
                statsvalue[statisticsType] = item[COUNT];
                mapAccumulator.put(filterObj, statsvalue);
            }
        }
    }
    private void accumulateInstallResults(List<Object> installResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(installResult, groupBy, mapAccumulator, TYPE_INSTALLS);
    }

    private void accumulateClickResults(List<Object> clickResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(clickResult, groupBy, mapAccumulator, TYPE_CLICKS);
    }

    private void accumulateDeliveryResults(List<Object> deliveryResult, String[] groupBy, HashMap<Object, Object> mapAccumulator) {
        accumulate(deliveryResult, groupBy, mapAccumulator, TYPE_DELIVERIES);
    }
}
