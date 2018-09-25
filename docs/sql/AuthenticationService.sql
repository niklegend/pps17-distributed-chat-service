-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 9.2.0_32              
-- * Generator date: Apr 23 2015              
-- * Generation date: Tue Sep 25 14:23:02 2018 
-- * LUN file: /Users/mattiavandi/Development/Courses/PPS/pps17-distributed-chat-service/docs/Services.lun 
-- * Schema: AuthenticationServiceSQL/1 
-- ********************************************* 


-- Database Section
-- ________________ 

use AuthService;


-- Tables Section
-- _____________ 

drop table if exists users;
create table users (
     username varchar(20) not null,
     password char(64) not null,
     constraint id_user primary key (username));


-- Constraints Section
-- ___________________ 


-- Index Section
-- _____________ 

