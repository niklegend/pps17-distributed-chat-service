-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.0              
-- * Generator date: Sep  6 2018              
-- * Generation date: Sun Sep 30 16:41:23 2018 
-- * LUN file: /Users/mattiavandi/Development/Courses/PPS/pps17-distributed-chat-service/docs/Services.lun 
-- * Schema: AuthenticationServiceSQL/1 
-- ********************************************* 


-- Database Section
-- ________________ 

use AuthService;


-- Tables Section
-- _____________ 

drop table if exists invalid_tokens;
create table invalid_tokens (
     token varchar(2048) not null,
     expirationDate date not null,
     constraint id_invalid_token primary key (token));

drop table if exists users;
create table users (
     username varchar(20) not null,
     password char(64) not null,
     constraint id_user primary key (username));


-- Constraints Section
-- ___________________ 


-- Index Section
-- _____________ 

