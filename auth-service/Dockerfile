# Base image OpenJDK 21
FROM eclipse-temurin:21-jdk
MAINTAINER dinhhuan <dinhhuan1357@gmail.com>

# Thư mục làm việc trong container
WORKDIR /app

# Copy JAR đã build từ target
COPY target/auth-service-0.0.1-SNAPSHOT.jar ./app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
