drop schema if exists `kh_sm`;
create schema `kh_sm`;
use kh_sm;

drop user if exists 'kh_sm'@'localhost';
create user 'kh_sm'@'localhost'
  identified by 'sKiLlet';
grant select, insert, update, delete on kh_sm.* TO 'kh_sm'@'localhost';


-- tables

create table Gender
(
	gender varchar(8) primary key not null
);

create table User
(
    user_id int primary key auto_increment not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    city varchar(32) null,
    wca_id varchar(16) null,
    phone_number varchar(32) null,
    gender varchar(8) not null,
    birth_date date null,
    approved date null,
    email varchar(64) not null unique,
    foreign key (gender) references Gender(gender)
);

create table Meeting
(
    meeting_id int primary key auto_increment not null,
    meeting_number int not null,
    `date` datetime not null unique
);

create table News
(
    news_id int primary key auto_increment not null,
    user_id int not null,
    `text` text not null,
    date_and_time datetime not null,
    foreign key (user_id) references user(user_id)
);

create table Counting
(
    counting varchar(8) primary key not null
);

create table Discipline
(
    discipline_id int primary key auto_increment not null,
    `name` varchar(32) not null,
    description text null,
    counting varchar(8) not null,
    foreign key (counting) references Counting(counting)
);

create table Result
(
    result_id int primary key auto_increment not null,
    average decimal(5, 2) null,
    meeting_id int not null,
    user_id int not null,
    discipline_id int not null,
    attempt_count int not null,
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
    password_hash binary(20) not null,
    disabled date null,
    foreign key (user_id) references User(user_id)
);

create table `Session`
(
    session_id int primary key auto_increment not null,
    user_id int not null,
    session_key varchar(64) not null unique,
    created datetime not null,
    foreign key (user_id) references Login(user_id)
);

-- views

create view meeting_results as
select
  m.meeting_id, m.meeting_number, m.date,
  d.discipline_id, d.name, d.description,
  r.result_id, r.average, r.attempt_count,
  u.user_id, u.first_name, u.last_name, u.city, u.gender, u.wca_id, u.phone_number, u.birth_date, u.approved,
  a.attempt_id, a.time
from meeting m
  inner join result r on m.meeting_id = r.meeting_id
  inner join discipline d on r.discipline_id = d.discipline_id
  inner join user u on r.user_id = u.user_id
  left join attempt a on r.result_id = a.result_id
order by m.meeting_id, d.discipline_id, r.average;

create view session_user as
select 
  s.session_id, s.session_key, s.created,
  l.password_hash, l.disabled,
  u.user_id, u.first_name, u.last_name, u.city, u.wca_id, u.phone_number, u.gender, u.birth_date, u.approved, u.email
from session s
inner join login l on s.user_id = l.user_id
inner join user u on l.user_id = u.user_id;
