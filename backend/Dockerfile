# 베이스 이미지: OpenJDK
FROM openjdk:21

# 작업 디렉토리
WORKDIR /app

# 소스 복사
COPY gradlew ./
COPY build.gradle ./
COPY settings.gradle ./
COPY gradle/ ./gradle
COPY src ./src

# gradlew 실행권한
RUN chmod +x gradlew

# 빌드
RUN ./gradlew build -x test

# 포트
ENV PORT=8080

# 실행
CMD ["java", "-jar", "build/libs/flow-0.0.1-SNAPSHOT.jar"]
