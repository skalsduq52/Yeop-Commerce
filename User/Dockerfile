# Base Image
FROM eclipse-temurin:17-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 캐시 사용을 위한 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Build
RUN ./gradlew build --no-daemon

# JAR 파일 실행
EXPOSE 8080
CMD ["java", "-jar", "build/libs/user-manage-0.0.1-SNAPSHOT.jar"]
