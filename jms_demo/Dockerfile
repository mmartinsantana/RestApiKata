# Auto
# mvn package -DskipTests

FROM eclipse-temurin:17.0.6_10-jre
EXPOSE 5672
RUN groupadd user && useradd -g user --create-home user
USER user
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]