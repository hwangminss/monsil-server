FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/monsil-server-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 6260

CMD ["java", "-jar", "/app/app.jar"]
