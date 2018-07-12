# adtech-system
A simple implementation of an AdTech web service that tracks the ads that are being delivered through their lifecycle and generates some simple statistics.

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
The system will have below services to fulfill the requirements

1. [Ingestion Service](https://github.com/gramcha/adtech-system/tree/master/ingest-service)
2. Redis cache
3. Kafka Data Pipeline 
4. [Store Service](https://github.com/gramcha/adtech-system/tree/master/store-service)
5. Mongodb
6. [Query Service](https://github.com/gramcha/adtech-system/tree/master/query-service) 

Refer the [System Diagram](https://raw.githubusercontent.com/gramcha/adtech-system/master/system-design-block-diagram.png)

To support the ingestion REST POST calls and statistics REST GET calls we need web services. Let's have two web services, namely Ingestion Service and Query Service.

[Ingestion Service](https://github.com/gramcha/adtech-system/tree/master/ingest-service) - It provides endpoints for ad tracker POST calls - delivery, click, install. We **flatten the data** of click and install before we send it to further processing.  The payload data will be pushed into Kafka topics. For simplicity Kafka topics created with one partition and one replication. Modify the docker-compose.yml file changing these configs.

In the ingestion POST request, the actual payload is having a reference to previous requests. For example, click payload will have the delivery id of the previous delivery request as reference id. We have to reject the click request if delivery of click request never received. Similarly, install payload has the reference click request. 

To handle this situation we need to keep track of previous requests. We can keep those requests in memory of ingestion service, or in SQL DB, or in some cache store.

- In-memory of ingestion service 
    - It will be faster for retrieval
    - It will be overhead for the service and soon system might face out of memory situation.
    - It will not work as expected if more than one ingestion services running in a load balancer
- In SQL
    - It can handle multiple ingestion services in load balanced environment.
    - Its retrieval will be slow compared to other solutions.
- In cache store
    - The cache stores are perfect for this kind of requirement.
    - Its retrieval will be faster
    - It can handle multiple ingestion services in load balanced environment.
    - The cache store can be scaled out in a distributed environment.

The cache store seems to be a better option than other two options. So we will have a cache store to store and retrieval of the previous request details. 

Addition to caching layer, this service will **flatten the data**. For example the click payload will have the corresponding delivery payload info like OS, browser, site, etc., 

Similarly, the install payload will have the click id and delivery payload info like OS, browser, site, etc., 

[Redis cache](https://redis.io/) - The ingestion service will store and retrieve the ad trackers from Redis cache.

We need to store these three payloads into the data store for later retrieval. Storing logic can be part of ingestion service itself or it can be delegated to another service. The delegation can be implemented as an ingestion service post that payloads to another service. This is tightly coupled service architecture. In the future, we have to modify the ingestion service if another service wants that data. It will become an unwanted problem for ingestion service. 

The better solution would be, ingestion service to push data into the data pipeline and let the other services integrate with the pipeline to consume the data.

[Kafka Data Pipeline](http://kafka.apache.org/) - We need to create three topics to store the three payloads namely delivery, click, and install.Reason for having three different topics are
    
- There are possibilities where Click and Install tracker never received in ingestion service. We will not get those trackers if a user did not click and install.
- three payloads might come in a different time interval

[Store Service](https://github.com/gramcha/adtech-system/tree/master/store-service) - It is a kafka consumer which will consume data from those three topics and store it into the data store. Kafka consumer configured with **auto-commit:false** and store service will commit once it is written into DB.

We have two options for the Data Store where the data can be retrieved easily. The options are
- SQL DB - 
    - Structured Query Language - we need to define data schema upfront.
    - It supports complex queries.
    - It is matured and supported by many programming languages.
    - It supports replication and sharding. For ex: MySQL
    - It requires the DB administrator for managing it.
- NoSQL DB
    - It supports dynamic schema for unstructured data.
    - Data is stored in many ways: it can be column-oriented, document-oriented, graph-based or organized as a KeyValue store.
    - We can create documents without having to first define their structure. You can add fields as you go.
    - It supports horizontal scalability, which helps reduce the workload and scale your business with ease.
    - Easy to manage there is no need for DB administrator.

**Considerations in choosing the DB**

In our case, the payload might get change easily. For example, to track the regional ad deliveries country code and city name can be added into the payload. In some cases, we may not have these fields in the tracker. To support this kind of flexibility we need to modify the DB schema if we use SQL. In terms, flexibility NoSQL is suitable for use. In terms of horizontal scalability, NoSQL is better than SQL. So we will use NoSQL for storing the tracker payload.

There are multiple choices for NoSQL DB includes MongoDB, BigTable, Redis, Cassandra, HBase, Neo4j, and CouchDB. For our requirement, MongoDB seems to a good choice for the following reasons
- It is an Open-Source database which is Document-oriented.
- MongoDB is a scalable and accessible database. 
- The JavaScript can be utilized as the query language.
- By utilizing sharding MongoDB scales horizontally.
- MongoDB is the most well known among NoSQL Databases.

[Mongodb](https://www.mongodb.com/) - The MongoDB will be used to store the data from the Kafka pipeline. It will be used by [Store Service](https://github.com/gramcha/adtech-system/tree/master/store-service) to store the data into three documents, namely, delivery, click, and install.

[Query Service](https://github.com/gramcha/adtech-system/tree/master/query-service) - It provides an endpoint for statistics query. It will query the three different documents with group by aggregating and merge the result to generate the desired response object.



### Docker deployment

All the services can be executed using docker compose. Use below steps for deploying it in docker

1. Setup **DOCKER_HOST_IP** as environment variable in order to communicate between docker containers.
    - execute below command
    ```sh
    export DOCKER_HOST_IP=`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1'`
    ```
    - verify the docker host ip by executing below command. It should return your ip address.
    ```sh
    echo $DOCKER_HOST_IP
    ```
    
2. Create executable jars for ingestion, store and query services.
    - execute below command
    ```sh
        sh maven_build.sh
    ```
    
3. Execute below docker compose command to run the containers
    ```sh
        docker-compose up --build -d
    ```

### API endpoints
- ingestion service - **port 8080**
    - http://localhost:8080/ads/delivery
    - http://localhost:8080/ads/click
    - http://localhost:8080/ads/install
- query service - **port 8282**
    -  http://localhost:8282/ads/statistics?start=2017-01-07T14:30:00+0000&end=2019-02-07T18:20:00+0000
    -  http://localhost:8282/ads/statistics?start=2017-01-07T14:30:00+0000&end=2019-02-07T18:20:00+0000&group_by=browser&group_by=os&group_by=site

Rest of the services like redis, mongo, kafka and zookeeper are running and exposing their default port. Please refer **docker-compose.yml** file for more details.

**Please make sure that data folder in project root directory is added into docker file sharing list of directories. Otherwise Redis and mongo might throw errors.**
