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

-- counting

SET @counting_avg5 = 'avg5';
insert into counting(counting)
values(@counting_avg5);

SET @counting_mo3 = 'mo3';
insert into counting(counting)
values(@counting_mo3);

SET @counting_bo3 = 'bo3';
insert into counting(counting)
values(@counting_bo3);

-- discipline

insert into discipline(`name`, description, counting) values ('3x3', 'Rubiks Cube is a 3-D combination puzzle invented in 1974 by Hungarian sculptor  and professor of architecture Ernő Rubik. Originally called the Magic Cube, the puzzle was licensed  by Rubik to be sold by Ideal Toy Corp. in 1980 via businessman Tibor Laczi and Seven Towns founder Tom Kremer, and won the German Game of the Year special award for Best Puzzle that year. As of January 2009, 350 million cubes  had been sold worldwide making it the worlds top-selling puzzle game. It is widely considered to be the  worlds best-selling toy. On a classic Rubiks Cube, each of the six faces is covered by nine stickers, each of one of six solid colours: white,  red, blue, orange, green, and yellow. In currently sold models, white is opposite yellow, blue is opposite green, and  orange is opposite red, and the red, white and blue are arranged in that order in a clockwise arrangement. On early  cubes, the position of the colours varied from cube to cube. An internal pivot mechanism enables each face to turn  independently, thus mixing up the colours. For the puzzle to be solved, each face must be returned to have only one colour.  Similar puzzles have now been produced with various numbers of sides, dimensions, and stickers, not all of them by Rubik.  Although the Rubiks Cube reached its height of mainstream popularity in the 1980s, it is still widely known and used. Many  speedcubers continue to practice it and other twisty puzzles and compete for the fastest times in various categories. Since 2003, The World Cube Association, the Rubiks Cubes international governing body, has organised competitions worldwide and  kept the official world records.', @counting_avg5);
SET @discipline_id_3_3 = last_insert_id();

insert into discipline(`name`, description, counting)  values ('2x2', 'The Pocket Cube (also known as the Mini Cube or the Ice Cube) is the 2×2×2 equivalent of a Rubiks Cube. The cube consists of 8 pieces, all corners.', @counting_avg5);
SET @discipline_id_2_2 = last_insert_id();

insert into discipline(`name`, description, counting)  values ('Skewb', 'The Skewb (/ˈskjuːb/) is a combination puzzle and a mechanical puzzle in the style of Rubiks Cube. It was invented by Tony Durham and marketed by Uwe Mèffert. Although it is cubical in shape, it differs from Rubiks construction in that its axis of rotation pass through the corners of the cube rather than the centres of the faces. There are four such axes, one for each space diagonal of the cube. As a result, it is a deep-cut puzzle in which each twist affects all six faces.  Mèfferts original name for this puzzle was the Pyraminx Cube, to emphasize that it was part of a series including his first tetrahedral puzzle Pyraminx. The catchier name Skewb was coined by Douglas Hofstadter in his Metamagical Themas column, and Mèffert liked it enough not only to market the Pyraminx Cube under this name but also to name some of his other puzzles after it, such as the Skewb Diamond.  Higher order Skewbs, named Master Skewb and Elite Skewb, have also been made.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('3x3 One-Handed', '3x3 which must be solved by one hand only', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('4x4', 'The 4x4x4 Cube (also known as Rubiks Revenge, and normally referred to as the 4x4x4 or 4x4) is a twisty puzzle in the shape of a cube that is cut three times along each of three axes.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('5x5', 'The 5x5x5 cube (also known as the Professors Cube, and normally referred to as the 5x5x5 or 5x5) is a twistable puzzle in the shape of a cube that is cut four times along each of three axes.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('Pyraminx', 'The Pyraminx is a triangular pyramid-shaped (or tetrahedron) puzzle. The parts are arranged in a pyramidal pattern on each side of the puzzle. The layers can be rotated with respect to each vertex, and the individual tips can be rotated as well. It was designed by Uwe Mèffert in the early 1970s.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('Clock', 'Rubiks Clock (often shortened to Clock) is a round, 2-sided puzzle that consists of 18 mini "clocks", 9 on each side, arranged in 3x3 squares. The hands on the clock can be manipulated by turning small wheels on the side of the puzzle and by pressing four buttons on the face. The object is to get them all pointing in the 12:00 position. The only commonly used type of Clock is the Rubiks brand. Since that company no longer produces this puzzle, to get one it is usually necessary to use eBay to find one. Fortunately this is a very common and inexpensive puzzle.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('Square-1', 'The Square-1 (short for Back to Square One, also known as Cube 21) is a cubic twisty puzzle, with irregular cubies. It is one of the most popular shape-shifting Rubiks Cube variants.', @counting_avg5);

insert into discipline(`name`, description, counting)  values ('6x6', 'The 6x6x6 cube (normally referred to as the 6x6x6 or 6x6) is a twistable puzzle in the shape of a cube that is cut five times along each of three axes. The only available brand for this puzzle was the V-Cube, which was patented by Panagiotis Verdes.', @counting_mo3);

insert into discipline(`name`, description, counting)  values ('7x7', 'The 7x7x7 cube (normally referred to as the 7x7x7 or 7x7) is a twistable puzzle in the shape of a cube that is cut six times along each of three axes. The first brand of this puzzle was the V-Cube 7, which was patented by its inventor, Panagiotis Verdes.', @counting_mo3);

insert into discipline(`name`, description, counting)  values ('3x3 Blindfolded', 'Blindfolded solving (abbreviated BLD) is the discipline of memorizing the position a puzzle is in and then solving it without looking at it again.', @counting_bo3);

insert into discipline(`name`, description, counting)  values ('3x3 Fewest Moves', 'Fewest Moves (or Fewest Moves Challenge, FMC) is an event where competitors attempt to solve a puzzle (almost always the 3x3x3) in as few moves as possible, starting from a given scramble.', @counting_bo3);

insert into discipline(`name`, description, counting)  values ('Megaminx', 'The Megaminx is a puzzle in the shape of a dodecahedron that was first produced by Uwe Meffert, who has the rights to some of the patents. Each of the 12 sides consists of one pentagonal fixed center, five triangular edge pieces and five corner pieces.', @counting_avg5);







