package com.gramcha.queryservice.controller

import com.gramcha.queryservice.service.QueryServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticsController {

    @Autowired
    private QueryServer queryServer;

    @RequestMapping(value = "/ads/statistics", method = RequestMethod.GET)
    getStatistics(@RequestParam(required = true, value = "start") String start,
                  @RequestParam(required = true, value = "end") String end,
                  @RequestParam(required = false, value = "group_by") String[] groupBy){
        return  queryServer.getStatistics(start,end,groupBy);
    }

    @RequestMapping("/ping")
    getTest() {
        return "ping pong";
    }

}
