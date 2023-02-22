# Auto
#    mvn spring-boot:build-image


# Manually:

# .\mvnw package -Pprod
# java -jar target/jms_demo-0.0.1-SNAPSHOT.jar

# docker build --tag=demo:latest .
# docker run --rm -t demo:latest

# Debugging?
# docker run --rm -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" jms_demo:latest

FROM eclipse-temurin:17.0.6_10-jre
EXPOSE 5672
RUN groupadd user && useradd -g user --create-home user
USER user
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]