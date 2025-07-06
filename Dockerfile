# Stage 1: Build
FROM amazoncorretto:17-alpine3.19-jdk AS builder

WORKDIR /app

COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:17-alpine3.19-jdk

WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

COPY --from=builder /app/target/ms-fisio-*.jar app.jar  

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
