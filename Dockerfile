FROM gradle:8.0.2-jdk17 AS mbuilder
COPY . /usr/src
WORKDIR /usr/src/
RUN gradle wrapper --gradle-version 8.0.2
RUN ./gradlew build

FROM openjdk:21-ea-17-jdk-slim
COPY --from=mbuilder /usr/src/build/libs/*.jar /usr/src/app.jar
ENTRYPOINT ["java","-jar","/usr/src/app.jar"]