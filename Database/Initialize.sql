drop schema if exists `kh_sm`;
create schema `kh_sm`;
use kh_sm;

drop user if exists 'kh_sm'@'localhost';
create user 'kh_sm'@'localhost'
  identified by 'sKiLlet';
grant select, insert, update, delete on kh_sm.* TO 'kh_sm'@'localhost';

create table User
(
    user_id int primary key auto_increment,
    first_name varchar(32) not null
);