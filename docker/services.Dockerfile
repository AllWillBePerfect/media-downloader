FROM denoland/deno:bin-2.8.0 AS deno

FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /workspace

COPY . .

RUN --mount=type=cache,target=/root/.gradle \
    chmod +x gradlew \
    && ./gradlew \
        :rest-app:bootJar \
        :download-service:bootJar \
        :delivery-service:bootJar \
        :telegram-app:bootJar \
        --no-daemon

FROM eclipse-temurin:21-jre-jammy AS rest-app

WORKDIR /app

COPY --from=builder /workspace/rest-app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-jammy AS delivery-service

WORKDIR /app

COPY --from=builder /workspace/delivery-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-jammy AS telegram-app

WORKDIR /app

COPY --from=builder /workspace/telegram-app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-jammy AS download-service

RUN apt-get update \
    && apt-get install --no-install-recommends -y ffmpeg python3 python3-venv \
    && python3 -m venv /opt/yt-dlp \
    && /opt/yt-dlp/bin/pip install --no-cache-dir "yt-dlp[default]" \
    && rm -rf /var/lib/apt/lists/*

COPY --from=deno /deno /usr/local/bin/deno

ENV PATH="/opt/yt-dlp/bin:${PATH}"

WORKDIR /app

COPY --from=builder /workspace/download-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
