# .\mvnw package
# java -jar target/demo-0.0.1-SNAPSHOT.jar

# docker build --tag=demo:latest .
# docker run -p8080:8080 demo:latest

FROM eclipse-temurin:17.0.6_10-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]