drop schema `kh_sm`;
create schema `kh_sm`;
use kh_sm;

drop user if exists 'kh_sm'@'localhost';
create user 'kh_sm'@'localhost'
  identified by 'sKiLlet';
grant select, insert, update, delete on kh_sm.* TO 'kh_sm'@'localhost';

CREATE TABLE User
(
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(32) NOT NULL
);