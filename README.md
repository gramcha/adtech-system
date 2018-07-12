# adtech-system
A simple implementation of a simple AdTech web service that tracks the ads that are being delivered through their lifecycle and generates some simple statistics.

The web service’s endpoints and request / response formats are described below.

### Ingestion
**POST /ads/delivery** that is triggered whenever one of our ads is loaded on a page and follows the spec below:
Request format:
        
        {
            "advertisementId": 4483,
            "deliveryId": "244cf0db-ba28-4c5f-8c9c-2bf11ee42988",
            "time": "2018-01-07T18:32:23.602300+0000",
            "browser": "Chrome",
            "os": "iOS",
            "site": "http://super-dooper-news.com"
        }

Response format:
- HTTP response code 200 if all went fine
- HTTP response code 500 if any error occurred

**POST /ads/click** that is triggered whenever an user clicks on one of the ads we delivered. It follows the spec below:
Request format:

        {
            "deliveryId": "244cf0db-ba28-4c5f-8c9c-2bf11ee42988",
            "clickId" : "fff54b83-49ff-476f-8bfb-2ec22b252c32",
            "time": "2018-01-07T18:32:34.201100+0000",
        }
Response format:
- HTTP response code 200 if everything went fine
- HTTP response code 404 if we never received the given delivery
- HTTP response 500 if something went wrong
 
**POST /ads/install** that follows the spec below:
Request format:
        
        {
            "installId": "144cf0db-ba28-4c5f-8c9c-2bf11ee42988",
            "clickId" : "fff54b83-49ff-476f-8bfb-2ec22b252c32",
            "time": "2018-01-07T18:32:34.201100+0000",
        }

Response format:
- HTTP response code 200 if everything went fine
- HTTP response code 404 if we never received the given click HTTP response 500 if something went wrong


### Statistics
**GET /ads/statistics?start={start}&end={end}**
Description: Returns the number of views, clicks and installs in the given period
**Example:** /ads/statistics?start=2018-01- 07T14:30:00+0000&end=2018-01-07T18:20:00+0000

Response format:

        {
            "interval" : {
                "start" : "2018-01-07T14:30:00+0000",
                "end" : "2018-01-07T18:20:00+0000"
            },
            "stats": {
                "deliveries": 10,
                "clicks": 4,
                "installs": 1
            }
        }

**GET /ads/statistics?start={start}&end={end}&group_by={category1}...&group_by={categoryN}**
Description returns the number of views, clicks and installs in the given
period grouped by the given categories
**Example: /ads/statistics?start=2018-01-07T14:30:00+0000&end=2018-01-07T18:20:00+0000&group_by=browser**

Response format:

        {
            "interval": {
                "start": "2018-01-07T14:30:00+0000",
                "end": "2018-01-07T18:20:00+0000"
            },
            "data": [
                {
                    "fields": {
                        "browser": "Chrome"
                    },
                    "stats": {
                        "deliveries": 10,
                        "clicks": 4,
                        "installs": 1
                    }
                },
                {
                    "fields": {
                        "browser": "Safari"
                    },
                    "stats": {
                        "deliveries": 2,
                        "clicks": 1,
                        "installs": 1
                    }
                }
            ]
        }


### System Design
To support the ingestion REST POST calls and statistics REST GET calls we need web services. Let's have two web services namely
1. [Ingestion Service](https://github.com/gramcha/adtech-system/tree/master/ingest-service) - It provides endpoints for ad tracker POST calls - delivery, click, install
2. [Query Service](https://github.com/gramcha/adtech-system/tree/master/query-service) - It provides endpoint for statistics query.

In ingestion POST request, actual payload is having reference of previous request. For example: Click payload will have the delivery id of previous delivery request as reference id. We have to reject the click request if deliveryid of click request never received. Similarly install payload have the reference click request. 

To handle this situation we need to keep track of previous requests. We can keep those requests in memory of ingestion service, or in some DB, or in some cache store.

- In memory of ingestion service 
    - It will be fast for reterival
    - It will be overhead for the service and soon system might face out of memory situation.
    - It will not work as expected if more than one ingestion services running in load balancer
- In some DB
    -  It can handle multiple ingestion services in load balancer environment.
    -  Its reterival will be slow compare to other solutions.
- In cache store
    - The cache stores are perfect for this kind of requirement.
    - Its reterival will be faster
    - It can handle multiple ingestion services in load balancer environment.
    - The cache store can be scaled out in distributed environment.

The cache store seems to be better option than other two options. So we will have a cache store to store and reterival of the previous request details.

3. [Redis cache](https://redis.io/) - The ingestion service will store and reterive the ad trackers.
