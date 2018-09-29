-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 9.2.0_32              
-- * Generator date: Apr 23 2015              
-- * Generation date: Tue Sep 25 14:43:56 2018 
-- * LUN file: /Users/mattiavandi/Development/Courses/PPS/pps17-distributed-chat-service/docs/Services.lun 
-- * Schema: UserServiceSQL/1 
-- ********************************************* 


-- Database Section
-- ________________ 

use UserService;


-- Tables Section
-- _____________ 

drop table if exists users;
create table users (
     username varchar(20) not null,
     first_name varchar(50) not null,
     last_name varchar(50) not null,
     bio varchar(1024) not null,
     visible char not null,
     last_seen date not null,
     constraint IDUser primary key (username));


-- Constraints Section
-- ___________________ 


-- Index Section
-- _____________ 

