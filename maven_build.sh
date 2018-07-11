cd ./ingest-service/
mvn clean install  -DskipTests
cd ..
cd ./store-service/
mvn clean install  -DskipTests
cd ..
cd ./query-service/
mvn clean install  -DskipTests
cd ..
