# === 빌드 스테이지 ===
# Java 21 JDK 이미지를 사용하여 애플리케이션 빌드
FROM eclipse-temurin:21-jdk-jammy AS builder

# 작업 디렉토리
WORKDIR /app

# Gradle Wrapper 및 소스 복사
COPY gradlew ./
COPY build.gradle ./
COPY settings.gradle ./
COPY gradle/ ./gradle
COPY src ./src

# gradlew 실행 권한
RUN chmod +x gradlew

# 프로젝트 빌드 (테스트 제외)
RUN ./gradlew clean build -x test

# === 실행 스테이지 ===
# 최소 JRE 이미지 사용하여 배포 컨테이너 경량화
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 빌드 산출물(JAR)만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Render 동적 포트 대응
ENV PORT=8080

# 컨테이너 포트 노출
EXPOSE 8080

# Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
