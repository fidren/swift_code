FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8080

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

COPY data/Interns_2025_SWIFT_CODES.csv /data/Interns_2025_SWIFT_CODES.csv

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
