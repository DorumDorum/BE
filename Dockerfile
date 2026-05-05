FROM gradle:8.10.2-jdk17 AS builder

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle* ./
COPY src src

RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "DEFAULT_JAVA_OPTS=\"-XX:StartFlightRecording=name=dorumdorum,settings=${JFR_SETTINGS:-profile},disk=true,dumponexit=true,filename=/app/logs/jfr/dorumdorum-${JFR_RUN_ID:-cloud-load-test}.jfr,maxage=${JFR_MAX_AGE:-30m},maxsize=${JFR_MAX_SIZE:-512m}\"; EFFECTIVE_JAVA_OPTS=\"${JAVA_OPTS:-$DEFAULT_JAVA_OPTS}\"; exec java ${EFFECTIVE_JAVA_OPTS} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} -jar /app/app.jar"]
