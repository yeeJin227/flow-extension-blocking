# 베이스 이미지: OpenJDK 21
FROM openjdk:21-jdk-slim

# 작업 디렉토리
WORKDIR /app

# Gradle Wrapper 및 소스 복사
COPY gradlew ./
COPY build.gradle ./
COPY settings.gradle ./
COPY gradle/ ./gradle
COPY src ./src

# 실행 권한 부여
RUN chmod +x gradlew

# 빌드
RUN ./gradlew build -x test

# 포트 설정
ENV PORT=8080

# 실행
CMD ["java", "-jar", "build/libs/flow-0.0.1-SNAPSHOT.jar"]
