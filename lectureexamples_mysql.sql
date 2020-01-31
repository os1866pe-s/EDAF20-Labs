-- Examples for SQL lectures in EDA216.
--
-- This can be executed in any database.
--
-- ------------------------------------------------------------------
-- Drop all tables.
-- ------------------------------------------------------------------

set FOREIGN_KEY_CHECKS = 0;
drop table if exists Accounts;
drop table if exists Movies;
drop table if exists MovieExecs;
drop table if exists Customers;
drop table if exists StarsIn;
drop table if exists Operas;
drop table if exists Stars;
drop table if exists Persons;
drop table if exists R;
drop table if exists S;
drop table if exists T;
drop table if exists Studios;

drop view if exists ParamountMovies;
set FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------------
-- Create all tables, insert data.
-- ------------------------------------------------------------------

create table Accounts (
   accountNo integer,
   balance   double,
   type      varchar(8),
   ownerPNo  char(11),
   primary key (accountNo)
);

insert into Accounts values(12345, 1000.00, 'savings', '730401-2334');
insert into Accounts values(67890, 2846.92, 'checking', '790614-2921');
insert into Accounts values(23588, -100.25, 'savings', '790614-2921');
insert into Accounts values(11611, 5010.00, 'savings', '801206-4321');
insert into Accounts values(21441, 4018.10, 'checking', '730401-2334');
insert into Accounts values(47118, -150.00, 'savings', '810423-2212');

create table Movies (
   title        varchar(25),
   year         integer,
   length       integer,
   filmType     varchar(10),
   studioName   varchar(20),
   prodNbr      integer,
   primary key (title, year)
);

insert into Movies
   values('Star Wars', 1977, 124, 'color', 'Fox', 12345);
insert into Movies
   values('Pretty Woman', 1990, 119, 'color', 'Disney', 99912);
insert into Movies
   values('Wayne''s World', 1992, 95, 'color', 'Paramount', 34129);
insert into Movies
   values('Mighty Ducks', 1991, 104, 'color', 'Disney', 45766);
insert into Movies
   values('Godfather, The', 1972, 175, 'color', 'Paramount', 43981);
insert into Movies
   values('Casablanca', 1942, 102, 'B/W', 'Warner Bros.', 28212);
insert into Movies
   values('Dr. Strangelove', 1964, 93, 'B/W', 'Columbia', 41006);
insert into Movies
   values('Some Like It Hot', 1959, 119, 'B/W', 'United Artists', 36093);
insert into Movies
   values('Apartment, The', 1960, 125, 'B/W', 'United Artists', 36093);
insert into Movies
   values('Philadelphia Story, The', 1940, 112, 'B/W', 'MGM', 12927);
insert into Movies
   values('King Kong', 1933, 100, 'B/W', 'RKO', 78411);
insert into Movies
   values('King Kong', 1976, 135, 'color', 'Paramount', 26561);
insert into Movies
   values('King Kong', 2005, 187, 'color', 'Universal Studios', 48881);

create table MovieExecs (
   name     varchar(20),
   address  varchar(20),
   certNbr  integer,
   netWorth double,
   primary key (certNbr)
);

insert into MovieExecs
   values('Steven Spielberg', 'Hollywood', 12345, 23000000.00);
insert into MovieExecs
   values('Albert S. Ruddy', 'Hollywood', 43981, 14000000.00);
insert into MovieExecs
   values('Hal B. Wallis', 'Hollywood', 28212, 4000000.00);
insert into MovieExecs
   values('Stanley Kubrick', 'Hollywood', 41006, 17000000.00);
insert into MovieExecs
   values('Billy Wilder', 'Hollywood', 36093, 2000000.00);
insert into MovieExecs
   values('Joseph L. Mankiewicz', 'Hollywood', 12927, 000000.00);
insert into MovieExecs
   values('David O. Selznick', 'Hollywood', 78411, 1000000.00);
insert into MovieExecs
   values('Dino De Laurentiis', 'Hollywood', 26561, 1000000.00);
insert into MovieExecs
   values('Jan Blenkin', 'Hollywood', 48881, 2000000.00);
insert into MovieExecs
   values('Per Holm', 'Malmö', 24412, 130.00);

create table Customers (
   persNo  char(11),
   name    varchar(20),
   address varchar(20),
   primary key (persNo)
);

insert into Customers
   values('730401-2334', 'Bo Ek', 'Malmö');
insert into Customers
   values('801206-4321', 'Eva Alm', 'Lund');
insert into Customers
   values('810423-2212', 'Sven Björck', 'Arlöv');

create table StarsIn (
   title    varchar(25),
   year     integer,
   starName varchar(20),
   primary key (title, year, starName)
);

insert into StarsIn
  values('Star Wars', 1977, 'Carrie Fisher');
insert into StarsIn
  values('Star Wars', 1977, 'Mark Hamill');
insert into StarsIn
  values('Star Wars', 1977, 'Harrison Ford');
insert into StarsIn
  values('Mighty Ducks', 1991, 'Emilio Estevez');
insert into StarsIn
  values('Wayne''s World', 1992, 'Dana Carvey');
insert into StarsIn
  values('Wayne''s World', 1992, 'Mike Myers');

create table R (
   a integer,
   b integer
);

create table S (
   b integer,
   c integer,
   d integer
);

insert into R
   values (1,2), (3, 4);

insert into S
   values (2,5,6), (4,7,8), (9,10,11);

create table Operas (
        composer        varchar(30),
        opera           varchar(30),
        primary key (composer,opera)
);

insert into Operas values('Verdi','Nabucco');
insert into Operas values('Verdi','La Traviata');
insert into Operas values('Bellini','Norma');
insert into Operas values('Puccini','Boheme');
insert into Operas values('Verdi','Rigoletto');
insert into Operas values('Puccini','Madama Butterfly');
insert into Operas values('Rossini','Guglielmo Tell');
insert into Operas values('Puccini','Il Trittico');
insert into Operas values('Verdi','Otello');
insert into Operas values('Bellini','La Favorita');

-- ------------------------------------------------------------------
-- SQL examples. Simple queries.
-- ------------------------------------------------------------------

select *
from Accounts;

select *
from Accounts
where accountNo = 67890;

select balance
from Accounts
where accountNo = 67890;

select accountNo
from Accounts
where type = 'savings' and balance < 0;

-- ------------------------------------------------------------------
-- SQL examples from book.
-- ------------------------------------------------------------------

select *
from Movies;

select *
from Movies
where studioName = 'Disney';

select *
from Movies
where studioName = 'Disney' and year = 1990;

select title, length
from Movies
where studioName = 'Disney' and year = 1990;

select title as name, length as duration
from Movies
where studioName = 'Disney' and year = 1990;

select title
from Movies
where year > 1970 and filmType <> 'color';

select title
from Movies
where year > 1950 and filmType <> 'color';

select title
from Movies
where title like 'Star%';

select *
from Movies
order by length, title;

select *
from Movies
where studioName = 'Disney' and year = 1990
order by length, title;

select *
from Movies
order by length, title
limit 5;

-- ------------------------------------------------------------------
-- Joins. Find the producer of Star Wars.
-- ------------------------------------------------------------------

select *
from Movies;

select *
from MovieExecs;

select prodNbr
from Movies
where title = 'Star Wars';

-- => 12345

select name
from MovieExecs
where certNbr = 12345;

select name
from Movies, MovieExecs
where title = 'Star Wars' and
      prodNbr = certNbr;

-- ------------------------------------------------------------------
-- More joins. Cartesian product, then join.
-- ------------------------------------------------------------------

select *
from Accounts;

select *
from Customers;

select *
from Customers, Accounts;

select *
from Customers, Accounts
where ownerPNo = persNo;

select *
from Customers, Accounts
where ownerPNo = persNo and
      accountNo = 12345;

select name
from Customers, Accounts
where ownerPNo = persNo and
      accountNo = 12345;

-- ------------------------------------------------------------------
-- Join, list movies with stars.
-- ------------------------------------------------------------------

select *
from Movies;

select *
from StarsIn;

select *
from Movies, StarsIn;

select *
from Movies, StarsIn
where Movies.title = StarsIn.title and
      Movies.year = StarsIn.year;

select Movies.title, Movies.year,
       length, filmType, studioName, starName
from Movies, StarsIn
where Movies.title = StarsIn.title and
      Movies.year = StarsIn.year;

-- ------------------------------------------------------------------
-- Union. No intersect or except in MySQL.
-- ------------------------------------------------------------------

select title, year
from Movies;

select title, year
from StarsIn;

select title, year from Movies
   union
select title, year from StarsIn;

select 1 union select 2;

select 1 union select 1;

-- ------------------------------------------------------------------
-- Subqueries. Find the producer of Star Wars. Find producers
-- of Harrison Ford movies.
-- ------------------------------------------------------------------

select name
from Movies, MovieExecs
where title = 'Star Wars' and
      prodNbr = certNbr;

select name
from MovieExecs
where certNbr =
      (select prodNbr
       from Movies
       where title = 'Star Wars');

select name
from MovieExecs
where certNbr in
      (select prodNbr
       from Movies
       where (title, year) in
             (select title, year
              from StarsIn
              where starName = 'Harrison Ford'));

select name
from MovieExecs, Movies, StarsIn
where certNbr = prodNbr and
      Movies.title = StarsIn.title and
      Movies.year = StarsIn.year and
      starName = 'Harrison Ford';

-- ------------------------------------------------------------------
-- Find producers who haven't produced any movies. Wrong way, right.
-- ------------------------------------------------------------------

select name
from MovieExecs, Movies
where certNbr <> prodNbr;

select name
from MovieExecs
where certNbr not in (select prodNbr from Movies);

-- ------------------------------------------------------------------
-- Find duplicate titles. Correlated subquery, then join on self.
-- ------------------------------------------------------------------

select title, year
from Movies Old
where year <> any
      (select year
       from Movies
       where title = Old.title);

select distinct Old.title, Old.year
from Movies Old, Movies New
where Old.title = New.title and
      Old.year <> New.year;

-- ------------------------------------------------------------------
-- Variations on joins.
-- ------------------------------------------------------------------

select *
from R;

select *
from S;

select *
from R, S;

select *
from R cross join S;

select *
from R, S
where R.b = S.b;

select *
from R natural join S;

select *
from R inner join S
   on R.b = S.b;

select *
from R right outer join S
   on R.b = S.b;

select *
from R left outer join S
   on R.b = S.b;

-- ------------------------------------------------------------------
-- Aggregation operators.
-- ------------------------------------------------------------------

select *
from MovieExecs;

select avg(netWorth)
from MovieExecs;

select *
from StarsIn;

select count(*)
from StarsIn;

select count(StarName)
from StarsIn;

select count(distinct starName)
from StarsIn;

select count(distinct title)
from StarsIn;

-- ------------------------------------------------------------------
-- Grouping.
-- ------------------------------------------------------------------

select *
from Movies;

select sum(length)
from Movies;

select studioName, sum(length)
from Movies
group by studioName;

select name, sum(length)
from MovieExecs, Movies
where prodNbr = certNbr
group by name;

-- ------------------------------------------------------------------
-- More grouping.
-- ------------------------------------------------------------------

select *
from Operas;

select composer, count(*)
from Operas
group by composer;

select composer, count(*)
from Operas
group by composer
order by count(*);

select composer, count(*)
from Operas
group by composer
having count(*) > 2;

select composer, count(*)
from Operas
where opera <> 'Otello'
group by composer
having count(*) > 2;

-- ------------------------------------------------------------------
-- Insertions, deletions.
-- ------------------------------------------------------------------

insert into StarsIn(title, year, starName)
   values('The Maltese Falcon', 1942, 'Sidney Greenstreet');

delete from StarsIn
where title = 'The Maltese Falcon' and
      year = 1942 and
      starName = 'Sidney Greenstreet';

insert into StarsIn
   values('The Maltese Falcon', 1942, 'Sidney Greenstreet');

delete from StarsIn
where starName = 'Sidney Greenstreet';

select *
from StarsIn;

update MovieExecs
   set netWorth = 2 * netWorth;

-- ------------------------------------------------------------------
-- Creating tables.
-- ------------------------------------------------------------------

create table Stars (
   name      char(30),
   address   varchar(256),
   gender    char(1) default '?',
   birthdate date
);

help data types;

describe Stars;

alter table Stars
   add phone char(16) default 'unlisted';

describe Stars;

alter table Stars
   drop birthdate;

-- ------------------------------------------------------------------
-- Indexes.
-- ------------------------------------------------------------------

show create table Movies;

create index YearIndex on Movies(year);

show create table Movies;

drop index YearIndex on Movies;

-- ------------------------------------------------------------------
-- Views.
-- ------------------------------------------------------------------

create view ParamountMovies as
   select title, year
   from Movies
   where studioName = 'Paramount';

select *
from ParamountMovies;

select title
from ParamountMovies
where year = 1976;

-- ------------------------------------------------------------------
-- Primary keys.
-- ------------------------------------------------------------------

create table Persons (
   persNo  char(11),
   name    varchar(40),
   address varchar(60),
   primary key (persNo)
);

insert into Persons
   values('730401-2334', 'Bo Ek', 'Malmö');
insert into Persons
   values('801206-4321', 'Eva Alm', 'Lund');

select *
from Persons;

insert into Persons
   values('730401-2334', 'Nils Björk', 'Arlöv');

drop table Persons;

create table Persons (
   persNo  char(11) primary key,
   name    varchar(40),
   address varchar(60)
);

-- ------------------------------------------------------------------
-- Invented keys, auto increment.
-- ------------------------------------------------------------------

create table T (
   x integer auto_increment,
   y varchar(10),
   primary key (x)
);

insert into T(y) values ('first');
insert into T values(0, 'second');
insert into T values(null, 'third');

select *
from T;

select last_insert_id();

-- ------------------------------------------------------------------
-- Foreign keys. MySQL: supported only by InnoDB.
-- ------------------------------------------------------------------

create table Studios (
   name char(30),
   address varchar(255),
   presCNbr int,
   primary key (name),
   foreign key (presCNBr) references MovieExecs(certNbr)
);

insert into Studios
   values ('Fox', 'Hollywood', 99999);

insert into Studios
   values ('Fox', 'Hollywood', 12345);

update Studios
   set presCNBr = 99999 where name = 'Fox';

delete from MovieExecs
   where certNbr = 12345;

