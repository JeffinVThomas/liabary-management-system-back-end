# -------- STAGE 1: Build --------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom first for caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# -------- STAGE 2: Runtime --------
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
