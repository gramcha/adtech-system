FROM java:8
#We have added a VOLUME pointing to /tmp because it is where a Spring Boot application creates working directories for Tomcat by default.
VOLUME /tmp
ADD ./target/store-service-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]