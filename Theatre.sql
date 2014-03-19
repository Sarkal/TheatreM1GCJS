/*
CANO Gregory
SEGUIN Jeremy
*/

drop table LesRepresentations;

create table LesRepresentations (
	noSpec number(3) check (noSpec > 0),
	dateSpec date,
	heureDeb number(2) check (0 <= heureDeb && heureDeb < 24),
	minDeb number(2) check (0 <= minDeb && minDeb < 60),
	constraint pk_LesRepresentations primary key (noSpec)
);
