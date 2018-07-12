# query-service
A simple implementation of a web service that queries the Ad trackers from Data Store. It queries MongoDb with given query params and returns the result response object

### Tech Stack
The Technical Stack used for this service is,
- Language - JAVA mixed with Groovy
- Spring Boot Web Application
- MongoDb - Spring Data Mongo
- Global Exception Handler - Spring Boot Hateoas

### Code Structure
This service is structured like below directory tree.
```sh
|-query-service
   |-src
   |---main
   |-----java
   |-------com
   |---------gramcha
   |-----------entities
   |-----------queryservice
   |-------------config
   |-------------controller
   |-------------exception
   |-------------repos
   |-------------service
   |-----resources
   |-------static
   |-------templates
   |---test
   |-----java
   |-------com
   |---------gramcha
   |-----------queryservice
   |-------------controller
   |-------------service

```
#### Explanation of structure
- src/main/java/com/gramcha is standard package strucutre.
- src/main/java/com/gramcha is standard package strucutre.
    - entities - It contains the entity classes (schema) of delivery, click, and Install. These classes are represented Mongo documents and same is used in the Kafka data schema deserialization.
    - queryservice - It contains the spring boot application class and other sub packages given below.
    - config - It provides MongoTemplate class bean and Mongo connection factory bean.
    - controller - It has RestController class which exposes the required endpoints.
    - exception - It has Hateoas based global exception controller of this web service. All the unhandled exceptions are handled here and the corresponding format response will be returned.
    - repos - It has repository interfaces and implementation classes to get entities from Mongo DB.
     - service - It has two service classes for the following activities
        -  AdTracker Query Service - It has the interface and implementation of the data store. It makes the simple statistics query and generate the response object. It delegates the groupby aggregate functionality to other service and generate response object for Group By results.
        -  GroupByQueryExecutorService Service Layer - It executes groupBy aggregation on three documents, namely delivery, click, and install. Then it will accumulate the query result into desired response.
    - resources - It has the application.yml file which has the details about Mongo DB, and server port details.
- test/main/java/com/gramcha/queryservice is standard package strucutre.
    - controller - It has a test class which will test the StatisticsController class with spring mockMVC.
    - repos - It has test classes to test the repository classes to validate the MongoDb Repos integration.
    - service - It has test classes to the core service class.

The web service’s endpoints and request / response formats are described below.

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
        

### API endpoints if you are running locally
     -  http://localhost:8282/ads/statistics?start=2017-01-07T14:30:00+0000&end=2019-02-07T18:20:00+0000
     -  http://localhost:8282/ads/statistics?start=2017-01-07T14:30:00+0000&end=2019-02-07T18:20:00+0000&group_by=browser&group_by=os&group_by=site
