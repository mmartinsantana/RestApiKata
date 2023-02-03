# RestApiKata
REST API Kata - https://docs.google.com/document/d/1Gyb9M_Ju_KhLeDeb3d4IFEnZUlbasO1Bu-xbcVbxxfU/edit



# REST API
## GET
It will pop up a login page from the browser, then you can enter the address again :/
### Show all users
```
http://localhost:8080/api/person/all
```
## Print out an account
```
http://localhost:8080/api/account/find/1
```
## POST
### Withdraw 2 from account id=2
Authoritation won't? work from the browser due to the redirection
#### Before
```
http://localhost:8080/api/account/withdrawal?account=1&amount=2
```
#### Now:
```
- http://localhost:8080/api/account/operation
{
  "type":"WITHDRAWAL",
  "amount":2.0,
  "account":{"id":2}
} 
```
### A REST client is required, such as curl
```
curl -u rest:restPass -d "accountId=2&amount=2" -X POST http://localhost:8080/api/account/withdrawal
```

#  Maven profiles

## Development profile - dev

mvn build -Pdev

### h2 database.
```
insert into person values (1,'Mario');
insert into account values (1, 1);
insert into operation values (1, 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 1);
ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 3
```

### Login to h2 console:
http://localhost:8080/h2-console/login.do

## Production profile - prod

mvn build -Pdev

### Postgres database.
```
insert into person values (nextval('HIBERNATE_SEQUENCE'),'Mario');
insert into account values (nextval('HIBERNATE_SEQUENCE'), 1);
insert into operation values (nextval('HIBERNATE_SEQUENCE'), 2, 2,  current_timestamp, 'DEPOSIT', 2);
```

# Running the application

## From the console
mvn spring-boot:build-image

## Using a docker container
```
.\mvnw package -Pprod
java -jar target/demo-0.0.1-SNAPSHOT.jar
docker build --tag=demo:latest .
docker run --rm -p8080:8080 -t demo:latest
```
Debugging?
```
docker run --rm -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t demo:latest
```
## Using user docker composer
 Docker-composer.yaml + Dockerfile
```
mvn package -Pprod
docker-compose up --build
docker-compose down 
```
# TODO
1. Run tests with h2 !! Test by running app first ;)
2. versioning https://stackoverflow.com/questions/20198275/how-to-manage-rest-api-versioning-with-spring
