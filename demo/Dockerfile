# Auto
#    mvn spring-boot:build-image


# Manually:

# .\mvnw package -Pprod
# java -jar target/demo-0.0.1-SNAPSHOT.jar

# docker build --tag=demo:latest .
# docker run --rm -p8080:8080 -t demo:latest

# Debugging?
# docker run --rm -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t demo:latest

FROM eclipse-temurin:17.0.6_10-jre
RUN groupadd user && useradd -g user --create-home user
USER user
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]