use kh_sm;
set SQL_SAFE_UPDATES = 0;

delete from attempt;
delete from result;
delete from meeting;
delete from discipline;
delete from counting;
delete from `user`;
delete from gender;

-- gender

insert into gender(gender)
values('male');

insert into gender(gender)
values('female');

-- users

insert into `user`(first_name, last_name, gender)
values ('Максим', 'Жемеренко', 'male');
SET @user_id_maxim = last_insert_id();

insert into `user`(first_name, last_name, gender)
values ('Маша', 'Мазел', 'female');
SET @user_id_masha = last_insert_id();

-- meetigs

insert into meeting(meeting_number, `date`) 
values (281, '2018-2-18');
set @meeting_id_1 = last_insert_id();

insert into meeting(meeting_number, `date`) 
values (280, '2018-2-11');
set @meeting_id_2 = last_insert_id();

insert into meeting(meeting_number, `date`) 
values (279, '2018-2-4');

insert into meeting(meeting_number, `date`) 
values (278, '2018-1-29');

insert into meeting(meeting_number, `date`) 
values (277, '2018-1-22');

-- counting

SET @counting_id_avg5 = 'avg5';
insert into counting(counting_id, attempt_count)
values(@counting_id_avg5, 5);

SET @counting_id_mo3 = 'mo3';
insert into counting(counting_id, attempt_count)
values(@counting_id_mo3, 3);

SET @counting_id_bo3 = 'bo3';
insert into counting(counting_id, attempt_count)
values(@counting_id_bo3, 3);

-- discipline

insert into discipline(`name`, description, counting_id) values ('3x3', 'Rubiks Cube is a 3-D combination puzzle invented in 1974 by Hungarian sculptor  and professor of architecture Ernő Rubik. Originally called the Magic Cube, the puzzle was licensed  by Rubik to be sold by Ideal Toy Corp. in 1980 via businessman Tibor Laczi and Seven Towns founder Tom Kremer, and won the German Game of the Year special award for Best Puzzle that year. As of January 2009, 350 million cubes  had been sold worldwide making it the worlds top-selling puzzle game. It is widely considered to be the  worlds best-selling toy. On a classic Rubiks Cube, each of the six faces is covered by nine stickers, each of one of six solid colours: white,  red, blue, orange, green, and yellow. In currently sold models, white is opposite yellow, blue is opposite green, and  orange is opposite red, and the red, white and blue are arranged in that order in a clockwise arrangement. On early  cubes, the position of the colours varied from cube to cube. An internal pivot mechanism enables each face to turn  independently, thus mixing up the colours. For the puzzle to be solved, each face must be returned to have only one colour.  Similar puzzles have now been produced with various numbers of sides, dimensions, and stickers, not all of them by Rubik.  Although the Rubiks Cube reached its height of mainstream popularity in the 1980s, it is still widely known and used. Many  speedcubers continue to practice it and other twisty puzzles and compete for the fastest times in various categories. Since 2003, The World Cube Association, the Rubiks Cubes international governing body, has organised competitions worldwide and  kept the official world records.', @counting_id_avg5);
SET @discipline_id_3_3 = last_insert_id();

insert into discipline(`name`, description, counting_id)  values ('2x2', 'The Pocket Cube (also known as the Mini Cube or the Ice Cube) is the 2×2×2 equivalent of a Rubiks Cube. The cube consists of 8 pieces, all corners.', @counting_id_mo3);
SET @discipline_id_2_2 = last_insert_id();

insert into discipline(`name`, description, counting_id)  values ('Skewb', 'The Skewb (/ˈskjuːb/) is a combination puzzle and a mechanical puzzle in the style of Rubiks Cube. It was invented by Tony Durham and marketed by Uwe Mèffert. Although it is cubical in shape, it differs from Rubiks construction in that its axis of rotation pass through the corners of the cube rather than the centres of the faces. There are four such axes, one for each space diagonal of the cube. As a result, it is a deep-cut puzzle in which each twist affects all six faces.  Mèfferts original name for this puzzle was the Pyraminx Cube, to emphasize that it was part of a series including his first tetrahedral puzzle Pyraminx. The catchier name Skewb was coined by Douglas Hofstadter in his Metamagical Themas column, and Mèffert liked it enough not only to market the Pyraminx Cube under this name but also to name some of his other puzzles after it, such as the Skewb Diamond.  Higher order Skewbs, named Master Skewb and Elite Skewb, have also been made.', @counting_id_bo3);

insert into discipline(`name`, description, counting_id)  values ('3x3 One-Handed', '3x3 which must be solved by one hand only', @counting_id_avg5);
