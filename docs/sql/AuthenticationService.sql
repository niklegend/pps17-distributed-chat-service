-- Database Section
-- ________________ 

use authservice;

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
