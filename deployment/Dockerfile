# =========
# 1) BUILD (Gradle oficial, sin wrapper)
# =========
FROM gradle:8.6.0-jdk17-alpine AS builder
ENV GRADLE_USER_HOME=/home/gradle/.gradle
WORKDIR /workspace
COPY settings.gradle* build.gradle* gradle.properties* main.gradle gradle/ ./
COPY domain/ ./domain/
COPY infrastructure/ ./infrastructure/
COPY applications/app-service/ ./applications/app-service/
RUN gradle --no-daemon -x test -p applications/app-service dependencies \
 && gradle --no-daemon -x test -p applications/app-service bootJar

# ==========
# 2) RUNTIME
# ==========
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /workspace/applications/app-service/build/libs/LoanRequest.jar /app/LoanRequest.jar
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["java","-jar","/app/LoanRequest.jar"]
