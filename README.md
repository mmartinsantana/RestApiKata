# RestApiKata
REST API Kata



# REST API
## GET will pop up a login page from the browser, then you can call it again.
http://localhost:8080/api/person/all
http://localhost:8080/api/account/find/1
## POST won't? work from the browser
http://localhost:8080/api/account/withdrawal?account=1&amount=2
## A REST client is required, such as curl
curl -u rest:restPass -d "accountId=2&amount=2" -X POST http://localhost:8080/api/account/withdrawal

#  Maven profiles

## dev (development, default)

mvn build -Pdev

### h2 database.
insert into person values (1,'Mario');
insert into account values (1, 1);
insert into operation values (1, 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 1);
ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 3

### Login to h2 console:
http://localhost:8080/h2-console/login.do

## prod (production)

mvn build -Pdev

### Postgres database.
insert into person values (nextval('HIBERNATE_SEQUENCE'),'Mario');
insert into account values (nextval('HIBERNATE_SEQUENCE'), 1);
insert into operation values (nextval('HIBERNATE_SEQUENCE'), 2, 2,  current_timestamp, 'DEPOSIT', 2);

# Running the application

## Run from the console:
mvn spring-boot:build-image

# Prepare a docker containe and run it
## Without Dockerfile

1. .\mvnw package
2. java -jar target/demo-0.0.1-SNAPSHOT.jar
3. docker build --tag=demo:latest .
4. docker run --rm -p8080:8080 -t demo:latest

Debugging?
docker run --rm -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t demo:latest

# Run user docker composer (docker-composer.yaml + Dockerfile)

mvn package -Pprod
docker-compose up --build
docker-compose down 
