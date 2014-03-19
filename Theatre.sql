/*
CANO Gregory
SEGUIN Jeremy
*/

drop table LesZones;
drop table LesCategories;
drop table LesPlaces;
drop table LesSpectacles;
drop table LesRepresentations;
drop table LesTickets;

create table LesZones (
	numZ number(3) check (numZ > 0),
	categorie ENUM('orchestre', 'balcon', 'poulailler'),
	constraint pk_LesZones primary key (numZ)
);

create table LesCategories (
	nomC ENUM('orchestre', 'balcon', 'poulailler'),
	prix number(3) check (prix > 0),
	constraint pk_LesCategories primary key (nomC)
);

create table LesPlaces (
	noPlace number(3) check (noPlace > 0),
	noRang number(3) check (noRang > 0),
	numZ number(3) check (numZ > 0),
	constraint pk_LesCategories primary key (noPlace, noRang)
);

create table LesSpectacles (
	numS number(3) check (noPlace > 0),
	nomS varchar(20),
	constraint pk_LesCategories primary key (numS)
);

create table LesRepresentations (
	numS number(3) check (noPlace > 0),
	dateRep date,
	constraint pk_LesCategories primary key (numS, dateRep)
);

create table LesTickets (
	noSerie number(3) check (noSerie > 0),
	numS number(3) check (noPlace > 0),
	dateRep date,
	noPlace number(3) check (noPlace > 0), 
	noRang number(3) check (noPlace > 0), 
	dateEmission date,
	constraint pk_LesCategories primary key (noSerie)
);

