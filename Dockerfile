FROM maven:3.8.5-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM registry.access.redhat.com/ubi9/openjdk-17
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
COPY --from=build /home/app/target/classes/logback.xml /usr/local/lib/logback.xml
EXPOSE 7070
ENTRYPOINT ["java","--add-opens=java.base/java.io=ALL-UNNAMED","--add-opens=java.base/java.nio=ALL-UNNAMED","-XX:InitialRAMPercentage=50.0","-XX:MaxRAMPercentage=75.0","-Djavax.net.debug=SSL","-jar","/usr/local/lib/app.jar"]
