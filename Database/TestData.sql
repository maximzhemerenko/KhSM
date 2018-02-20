use kh_sm;
set SQL_SAFE_UPDATES = 0;

delete from attempt;
delete from result;
delete from `user`;
delete from meeting; 
delete from discipline; 


-- users

insert into `user`(first_name, last_name, gender) 
values ('Максим', 'Жемеренко', 'male');
SET @user_id = last_insert_id();


-- meetigs

insert into meeting(meeting_number, `date`) 
values (281, '2018-2-18');
set @meeting_id = last_insert_id();

insert into meeting(meeting_number, `date`) 
values (280, '2018-2-11');

insert into meeting(meeting_number, `date`) 
values (279, '2018-2-4');

insert into meeting(meeting_number, `date`) 
values (278, '2018-1-29');

insert into meeting(meeting_number, `date`) 
values (277, '2018-1-22');


-- disciplines
 
insert into discipline(`name`, description, attempt_count)
values ('3x3', 'Rubiks Cube is a 3-D combination puzzle invented in 1974 by Hungarian sculptor  and professor of architecture Ernő Rubik. Originally called the Magic Cube, the puzzle was licensed  by Rubik to be sold by Ideal Toy Corp. in 1980 via businessman Tibor Laczi and Seven Towns founder Tom Kremer, and won the German Game of the Year special award for Best Puzzle that year. As of January 2009, 350 million cubes  had been sold worldwide making it the worlds top-selling puzzle game. It is widely considered to be the  worlds best-selling toy. On a classic Rubiks Cube, each of the six faces is covered by nine stickers, each of one of six solid colours: white,  red, blue, orange, green, and yellow. In currently sold models, white is opposite yellow, blue is opposite green, and  orange is opposite red, and the red, white and blue are arranged in that order in a clockwise arrangement. On early  cubes, the position of the colours varied from cube to cube. An internal pivot mechanism enables each face to turn  independently, thus mixing up the colours. For the puzzle to be solved, each face must be returned to have only one colour.  Similar puzzles have now been produced with various numbers of sides, dimensions, and stickers, not all of them by Rubik.  Although the Rubiks Cube reached its height of mainstream popularity in the 1980s, it is still widely known and used. Many  speedcubers continue to practice it and other twisty puzzles and compete for the fastest times in various categories. Since 2003, The World Cube Association, the Rubiks Cubes international governing body, has organised competitions worldwide and  kept the official world records.', 5); 
SET @discipline_id = last_insert_id();


-- results

insert into result(meeting_id, user_id, discipline_id, average)
values(@meeting_id, @user_id, @discipline_id, 100.64);
set @result_id = last_insert_id();

insert into attempt(result_id, `time`)
values(@result_id, 95.64);

insert into attempt(result_id, `time`)
values(@result_id, 97.64);

insert into attempt(result_id, `time`)
values(@result_id, 99.64);

insert into attempt(result_id, `time`)
values(@result_id, 101.64);

insert into attempt(result_id, `time`)
values(@result_id, 103.64);
