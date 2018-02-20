drop schema if exists `kh_sm`;
create schema `kh_sm`;
use kh_sm;

drop user if exists 'kh_sm'@'localhost';
create user 'kh_sm'@'localhost'
  identified by 'sKiLlet';
grant select, insert, update, delete on kh_sm.* TO 'kh_sm'@'localhost';


-- tables

create table User
(
    user_id int primary key auto_increment not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    city varchar(32) null,
    wca_id varchar(16) null,
    phone_number varchar(16) null,
    gender varchar(8) not null,
    birth_date date null,
    approved date null
);

create table Meeting
(
    meeting_id int primary key auto_increment not null,
    meeting_number int not null,
    `date` date not null
);

create table News
(
    news_id int primary key auto_increment not null,
    user_id int not null,
    `text` text not null,
    date_and_time datetime not null,
    foreign key (user_id) references user(user_id)
);

create table Discipline
(
	  discipline_id int primary key auto_increment not null,
    `name` varchar(32) not null,
    description text null,
    attempt_count int not null
);

create table Result
(
    result_id int primary key auto_increment not null,
    average decimal(5, 2) null,
    meeting_id int not null,
    user_id int not null,
    discipline_id int not null,
    foreign key (meeting_id) references Meeting(meeting_id),
    foreign key (user_id) references User(user_id),
    foreign key (discipline_id) references Discipline(discipline_id),
    unique(meeting_id, user_id, discipline_id)
);

create table Attempt
(
    attempt_id int primary key auto_increment not null,
    result_id int not null,
    `time` decimal(5, 2) null,
    foreign key (result_id) references Result(result_id)
);

create table Role
(
    role_id int primary key auto_increment not null,
    `name` varchar(16) not null unique,
    role_key varchar(16) not null unique
);

create table User_Role
(
    user_id int primary key auto_increment not null,
    role_id int not null,
    foreign key (user_id) references User(user_id),
    foreign key (role_id) references Role(role_id)
);

create table Login
(
    user_id int primary key auto_increment not null unique,
    password_hash binary not null,
    password_salt int not null,
    disabled date null,
    foreign key (user_id) references User(user_id)
);

create table `Session`
(
    session_id int primary key auto_increment not null,
    user_id int not null,
    session_key binary not null unique,
    created date not null,
    foreign key (user_id) references User(user_id)
);


-- views

create view meeting_discipline as
select 
  m.meeting_id, m.meeting_number, m.date, 
  d.discipline_id, d.name, d.description, d.attempt_count
from meeting m
  inner join result r on m.meeting_id = r.meeting_id
  inner join discipline d on r.discipline_id = d.discipline_id
group by m.meeting_id, d.discipline_id;