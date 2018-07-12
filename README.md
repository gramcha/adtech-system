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


