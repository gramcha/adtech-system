package com.gramcha.queryservice.service

import com.gramcha.entities.ClickTracker
import com.gramcha.entities.DeliveryTracker
import com.gramcha.entities.InstallTracker
import com.gramcha.queryservice.repos.ClickTrackerRepository
import com.gramcha.queryservice.repos.DeliveryTrackerRepository
import com.gramcha.queryservice.repos.InstallTrackerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

import javax.xml.bind.DatatypeConverter


/**
 * Created by gramcha on 11/07/18.
 */
@Service
class AdTrackerQueryService implements QueryServer {
    public static final String FIELDS = "fields"
    public static final String STATS = "stats"
    public static final String INTERVAL = "interval"
    public static final String DATA = "data"
    public static final String TYPE_INSTALLS = "installs"
    public static final String TYPE_CLICKS = "clicks"
    public static final String TYPE_DELIVERIES = "deliveries"
    public static final String START = "start"
    public static final String END = "end"

    @Autowired
    DeliveryTrackerRepository dRepo;

    @Autowired
    ClickTrackerRepository cRepo;

    @Autowired
    InstallTrackerRepository iRepo;

    @Autowired
    GroupByQueryExecutorService groupByQueryExecutorService;

    Object getStatistics(String start, String end, String[] groupBy) {
        if(StringUtils.isEmpty(start) || StringUtils.isEmpty(end))
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
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
        HashMap<Object, Object> mergedResult =  groupByQueryExecutorService.execute(start,end,groupBy);
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
        stats[TYPE_DELIVERIES] = dResult.size();
        stats[TYPE_CLICKS] = cResult.size();
        stats[TYPE_INSTALLS] = iResult.size();

        def resultObj = [:];
        resultObj[INTERVAL] = interval;
        resultObj[STATS] = stats;
        return resultObj
    }

    private LinkedHashMap createIntervalObject(String start, String end) {
        def interval = [:];
        interval[START] = start;
        interval[END] = end;
        return interval;
    }
}
