# Base Image
FROM eclipse-temurin:17-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Build
COPY build/libs/User-0.0.1-SNAPSHOT.jar /app/UserManage.jar

ENV TZ=Asia/Seoul

CMD ["java", "-jar", "-Dspring.profiles.active=server", "-Duser.timezone=Asia/Seoul", "/app/UserManage.jar"]