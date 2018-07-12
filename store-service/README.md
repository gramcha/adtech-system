# store-service
A simple implementation of a web service that consumes the Ad trackers produced by ingestion service from the Data Pipeline. The consumed data will be stored in the Data Store for further retrieval. Kafka autocommit configured as false to ensure at least once behaviour. Kafa data will be committed after saving into MongoDb.

### Tech Stack
The Technical Stack used for this service is,
- Language - JAVA
- Spring Boot Web Application
- Kafka Cosumer - Spring Kafka Listner - atleast once configuration
- MongoDB - Spring Data Mongo

### Code Structure
This service is structured like below directory tree.
```sh
|-store-service
    |-src
       |---main
       |-----java
       |-------com
       |---------gramcha
       |-----------entities
       |-----------storeservice
       |-------------config
       |-------------repos
       |-------------service
       |-----resources
       |-------static
       |-------templates
       |---test
       |-----java
       |-------com
       |---------gramcha
       |-----------storeservice
```
#### Explanation of structure
- src/main/java/com/gramcha is standard package strucutre.
    - entities - It contains the entity classes (schema) of delivery, click, and Install. These classes are represented Mongo documents and same is used in the Kafka data schema deserialization.
    - storeservice - It contains the spring boot application class and other sub packages given below.
    - config - It provides MongoTemplate class bean and Mongo connection factory bean.
    - repos - It has repository interfaces and implementation classes to store entities into Mongo DB.
     - service - It has two service classes for the following activities
        -  Data store Service Layer - It has the interface and implementation of the data store. Currently, Mongo DB is underlying implementation and the same can be replaced with another implementation without affecting core service.
        -  Data Pipeline Service Layer - It has the interface and implementation of consuming data from the data pipeline. Currently, Kafka is the underlying implementation same can be replaced with another implementation without affecting core service.
    - resources - It has the application.yml file which has the details about Mongo DB, Kafka consumer, and server port details.

