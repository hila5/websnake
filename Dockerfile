# Run stage only - using pre-built jar
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Create non-root user (best practice for OpenShift)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy pre-built jar from target folder
COPY target/hila-websnake-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

# JVM args from your pom.xml config
ENTRYPOINT ["java", \
  "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
  "-jar", "/app/app.jar"]