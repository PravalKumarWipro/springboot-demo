FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM registry.access.redhat.com/ubi8/openjdk-11 
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
EXPOSE 7070
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
