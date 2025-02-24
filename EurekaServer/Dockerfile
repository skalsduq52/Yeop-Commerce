# Base Image
FROM eclipse-temurin:17-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Build
COPY build/libs/EurekaServer-0.0.1-SNAPSHOT.jar /app/EurekaServer.jar

ENV TZ=Asia/Seoul

CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app/EurekaServer.jar"]