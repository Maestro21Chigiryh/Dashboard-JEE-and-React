FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package

FROM jetty:9.4-jre17
COPY --from=build /app/target/*.war /var/lib/jetty/webapps/root.war

EXPOSE 8080