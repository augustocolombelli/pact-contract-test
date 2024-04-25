FROM gradle:8.6.0-jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle clean assemble --no-daemon

FROM eclipse-temurin:17-jdk-alpine

COPY --from=build /home/gradle/src/build/libs/person-data-service-1.0.0.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]