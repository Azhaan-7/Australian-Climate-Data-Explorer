FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY database/climate.db.zip ./database/climate.db.zip

RUN mvn clean package -DskipTests

RUN cd database \
    && jar xf climate.db.zip \
    && rm climate.db.zip


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/studio-project-eqv-sep23-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/database/climate.db ./database/climate.db

EXPOSE 7001

CMD ["java", "-jar", "app.jar"]