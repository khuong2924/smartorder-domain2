FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src/ /app/src/
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN apt-get update && apt-get install -y curl

# Sử dụng ARG để nhận giá trị từ docker-compose
ARG JWT_SECRET
ARG JWT_EXPIRATION=86400000
ARG SERVER_FORWARD_HEADERS_STRATEGY=framework

ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_EXPIRATION=${JWT_EXPIRATION}
ENV SERVER_FORWARD_HEADERS_STRATEGY=${SERVER_FORWARD_HEADERS_STRATEGY}
ENV TZ=Asia/Ho_Chi_Minh


HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/domain2/actuator/health || exit 1

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]