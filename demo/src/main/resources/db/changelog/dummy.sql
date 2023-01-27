insert into person values (1,'Mario');
insert into account values (1, 1);
insert into operation values (1, 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 1);
ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 3



insert into person values (nextval('HIBERNATE_SEQUENCE'),'Mario');
insert into account values (nextval('HIBERNATE_SEQUENCE'), 1);
insert into operation values (nextval('HIBERNATE_SEQUENCE'), 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 2);

# http://localhost:8080/h2-console/login.do

# http://localhost:8080/api/person/all
# http://localhost:8080/api/account/find/1
# http://localhost:8080/api/account/withdrawal?account=1&amount=2


# curl -d "accountId=1&amount=2" -X POST http://localhost:8080/api/account/withdrawal

# curl -u rest:restPassword -d "accountId=1&amount=2" -X POST http://localhost:8080/api/account/withdrawal

