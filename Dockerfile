FROM java:8
VOLUME /tmp
RUN mkdir -p /app/ && mkdir -p /app/logs/
ADD target/car-orders-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar", "-Dspring.config.location=classpath:/config/application.properties"]