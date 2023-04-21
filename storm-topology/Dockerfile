FROM maven:3.8.2-jdk-11

WORKDIR /app

COPY pom.xml pom.xml
COPY src ./src

RUN mvn clean package
