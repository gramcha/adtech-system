package com.gramcha.queryservice.service

/**
 * Created by gramcha on 11/07/18.
 */
interface QueryServer {
    Object getStatistics(String start, String end, String[] groupBy)
}