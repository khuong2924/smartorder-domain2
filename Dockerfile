FROM openjdk:17-jdk-slim

WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/domain2-0.0.1-SNAPSHOT.jar"]