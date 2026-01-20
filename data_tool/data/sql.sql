delete from downloads;
delete from images;
delete from games;
delete from game_genres; 
delete from game_platforms; 
delete from genres; 
delete from platforms;
delete from developers;
delete from publishers;
delete from flyway_schema_history;

drop table downloads;
drop table game_genres;
drop table game_platforms;
drop table genres;
drop table platforms;
drop table images;
drop table games;
drop table developers;
drop table publishers;

select * from downloads;
select * from games;
select * from game_genres; 
select * from game_platforms;
select * from images;
select * from developers;
select * from publishers;
select * from flyway_schema_history;