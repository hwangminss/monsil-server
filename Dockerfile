FROM openjdk:17-jdk-slim

# 시간대 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

WORKDIR /app

COPY build/libs/monsil-server-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 6260

CMD ["java", "-jar", "/app/app.jar"]
