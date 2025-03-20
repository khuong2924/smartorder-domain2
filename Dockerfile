FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN apt-get update && apt-get install -y libtcnative-1 curl

ENV JWT_SECRET=8K9sJ2mPqX7vL4rT5nY8uW3iB6oZ9cA1dF4gH7jK
ENV JWT_EXPIRATION=86400000

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]