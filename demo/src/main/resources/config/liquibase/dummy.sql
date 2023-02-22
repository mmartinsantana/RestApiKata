insert into person values (1,'Mario');
insert into account values (1, 1);
insert into operation values (1, 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 1);
ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 3



insert into person values (nextval('HIBERNATE_SEQUENCE'),'Mario');
insert into account values (nextval('HIBERNATE_SEQUENCE'), 1);
insert into operation values (nextval('HIBERNATE_SEQUENCE'), 2, 2,  current_timestamp, 'DEPOSIT', 2);


# h2
# insert into operation values (nextval('HIBERNATE_SEQUENCE'), 2, 2,  {ts '2012-09-17 18:47:52.69'}, 'DEPOSIT', 2);

# http://localhost:8080/h2-console/login.do

# http://localhost:8080/api/person/all
# http://localhost:8080/api/account/find/1
# http://localhost:8080/api/account/withdrawal?account=1&amount=2


# curl -d "accountId=2&amount=2" -X POST http://localhost:8080/api/account/withdrawal

# curl -u rest:restPass -d "accountId=2&amount=2" -X POST http://localhost:8080/api/account/withdrawal



select * from person;
select * from account;
select * from operation;

ALTER SEQUENCE account_sequence RESTART WITH 200;
SELECT NEXT VALUE FOR account_sequence;
SELECT CURRENT VALUE FOR account_sequence;
INSERT INTO account (id, person_id) VALUES (NEXT VALUE FOR account_sequence, 1);

select * from account




*insert into authority (id, authority) values (0, 'a');*/
/*insert into authority (id,authority) values
(1,'ROLE_USER'),
(2,'ROLE_REST'),
(3,'ROLE_ADMINISTRATOR');

insert into app_user (id,user_name,pass) values
(1,'pp','$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.');

insert into app_user_authorities (app_user_id,authorities_id) values
(1,1),
(1,2);*/

select * from authority;

select * from app_user;
