# ingest-service
A simple implementation of a web service that ingests the Ad trackers produced by mobiles and other devices. It will receive Ad tracking events from different devices and push it to the Kafka data pipeline for further processing.

### Tech Stack
The Technical Stack used for this service is,
- Language - JAVA
- Spring Boot Web Application
- Kafka Producer - Spring Kafka Template
- Redis Cache - Spring Data Redis
- Global Exception Handler - Spring boot hateoas

### Code Structure
This service is structured like below directory tree.
```sh
|-ingest-service
   |-src
   |---main
   |-----java
   |-------com
   |---------gramcha
   |-----------entities
   |-----------ingestservice
   |-------------config
   |-------------controller
   |-------------exception
   |-------------models
   |-------------repos
   |-------------service
   |-----resources
   |-------static
   |-------templates
   |---test
   |-----java
   |-------com
   |---------gramcha
   |-----------ingestservice
   |-------------controller
   |-------------repos
   |-------------service
```
#### Explanation of structure
- src/main/java/com/gramcha is standard package strucutre.
    - entities - It contains the entity classes (schema) of delivery, click, and Install. These classes are represented Redis entities and same is used in the Kafka data schema.
    - ingestservice - It contains the spring boot application class and other sub packages given below.
    - config - It provides RedisTemplate class bean and Redis connection factory bean.
    - controller - It has RestController class which exposes the required endpoints.
    - exception - It has Hateoas based global exception controller of this web service. All the unhandled exceptions are handled here and the corresponding format response will be returned.
    - models - It has the DTO classes which will be received in exposed POST endpoints.
    - repos - It has repository interfaces and implementation classes to store entities into Redis cache and retrieve the same.
     - service - It has three service classes for the following activities
        - Tracker Ingest Service - It will do the following activities
            - Stores the received Delivery Tracker into Redis cache and pushed into data pipeline.
            - When receiving click tracker, it will query cache service with given delivery id. if found the match it will create a Click Entity instance, which will have the delivery tracker details too. Newly created click data is pushed into Redis cache and pushed into the data pipeline.
            - When receiving install tracker, it will query cache service with given click id. if found the match it will create an Install Entity instance, which will have the delivery tracker, click tracker details too. Newly created install data is pushed into the data pipeline.
        -  Data Cache Service Layer - It has the interface and implementation of caching. Currently, Redis cache is underlying implementation and the same can be replaced with another implementation without affecting core service.
        -  Data Pipeline Service Layer - It has the interface and implementation of pushing data into the data pipeline. Currently, Kafka is the underlying implementation same can be replaced with another implementation without affecting core service.
    - resources - It has the application.yml file which has the details about Redis cache, Kafka producer, and server port details.
- test/main/java/com/gramcha/ingestservice is standard package strucutre.
    - controller - It has test class which will test the AdTrackerController class with spring mockMVC.
    - repos - It has test classes to test the repository classes to validate the Redis Repos.
    - service - It has test classes to the core and other service classes.


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

### Swagger API Support
Swagger API support added. Please hit below endpoints in browser
    - http://localhost:8080/swagger-ui.html


### API endpoints if you are running locally
    - http://localhost:8080/ads/delivery
    - http://localhost:8080/ads/click
    - http://localhost:8080/ads/install
