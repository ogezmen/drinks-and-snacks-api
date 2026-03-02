FROM gradle:9.3.0-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/drink-and-snack-api-all.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]