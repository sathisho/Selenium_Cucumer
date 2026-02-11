# Multi-stage Docker build for Selenium Cucumber Framework

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Install required dependencies for Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    chromium-browser \
    chromium-driver \
    firefox \
    firefox-geckodriver \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven and dependencies from builder
COPY --from=builder /root/.m2 /root/.m2
COPY --from=builder /app/target ./target

# Copy source and configuration files
COPY --from=builder /app/src ./src
COPY --from=builder /app/pom.xml .
COPY testng.xml .
COPY src/test/resources ./src/test/resources

# Create directories for reports and screenshots
RUN mkdir -p test-output/reports test-output/screenshots logs

# Set environment variables
ENV DISPLAY=:99
ENV HEADLESS=true
ENV BROWSER=chrome

# Expose port for reports (optional)
EXPOSE 8080

# Run tests
CMD ["mvn", "clean", "test", "-Dcucumber.filter.tags=@smoke", "-Dbrowser=${BROWSER}", "-Dheadless=${HEADLESS}"]
