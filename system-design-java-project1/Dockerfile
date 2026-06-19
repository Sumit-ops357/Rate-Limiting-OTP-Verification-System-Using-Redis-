# Build stage
FROM gradle:8.14.2-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Install curl for health checks
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Copy jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]