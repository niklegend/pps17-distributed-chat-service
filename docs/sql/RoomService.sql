-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 9.2.0_32              
-- * Generator date: Apr 23 2015              
-- * Generation date: Tue Sep 25 14:25:12 2018 
-- * LUN file: /Users/mattiavandi/Development/Courses/PPS/pps17-distributed-chat-service/docs/Services.lun 
-- * Schema: RoomServiceSQL/1 
-- ********************************************* 


-- Database Section
-- ________________ 

use RoomService;


-- Tables Section
-- _____________ 

drop table if exists messages;
create table messages (
     user_name varchar(20) not null,
     room_name varchar(50) not null,
     timestamp date not null,
     content varchar(1024) not null,
     constraint id_message primary key (user_name, room_name, timestamp));

drop table if exists participations;
create table participations (
     user_name varchar(20) not null,
     room_name varchar(50) not null,
     join_date date not null,
     constraint id_participation primary key (user_name, room_name));

drop table if exists rooms;
create table rooms (
     name varchar(50) not null,
     owner_name varchar(20) not null,
     constraint id_room primary key (name));

drop table if exists users;
create table users (
     username varchar(20) not null,
     constraint id_user primary key (username));


-- Constraints Section
-- ___________________ 

alter table messages add constraint FKPM
     foreign key (user_name, room_name)
     references participations (user_name, room_name);

alter table participations add constraint FKPR
     foreign key (room_name)
     references rooms (name);

alter table participations add constraint FKUP
     foreign key (user_name)
     references users (username);

alter table rooms add constraint FKOR
     foreign key (owner_name)
     references users (username);


-- Index Section
-- _____________ 

