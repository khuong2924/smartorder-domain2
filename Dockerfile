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

# Không ghi đè application.yml, sử dụng biến môi trường để cấu hình
ENV JWT_SECRET=8K9sJ2mPqX7vL4rT5nY8uW3iB6oZ9cA1dF4gH7jK
ENV JWT_EXPIRATION=86400000
ENV SERVER_FORWARD_HEADERS_STRATEGY=framework

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]