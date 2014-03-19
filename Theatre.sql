/*
CANO Gregory
SEGUIN Jeremy
*/
-- start ScriptZoo
drop table LesHistoiresAff;
drop table LesMaladies;
drop table LesAnimaux;
drop table LesResponsables;
drop table LesGardiens;
drop table LesEmployes;
drop table LesCages;


create table LesCages (
	noCage number(3) check (noCage > 0),
	fonction varchar2(20),
	noAllee number(3) check (noAllee > 0),
	constraint pk_LesCages primary key (noCage)
);

create table LesEmployes (
	nomE varchar2(20),
	adresse varchar2(20),
	constraint pk_LesEmployes primary key (nomE)
);

create table LesGardiens (
	noCage number(3) check (noCage > 0),
	nomE varchar2(20),
	constraint pk_LesGardiens primary key (noCage, nomE),
	constraint fk_LesGardiens_noCage foreign key (noCage) references LesCages(noCage),
	constraint fk_LesGardiens_nomE foreign key (nomE) references LesEmployes(nomE)
);

create table LesResponsables (
	noAllee number(3) check (noAllee > 0),
	nomE varchar2(20),
	constraint pk_LesResponsables primary key (noAllee),
	constraint fk_LesResponsables_nomE foreign key (nomE) references LesEmployes(nomE)
);

create table LesAnimaux (
	nomA varchar2(20),
	sexe varchar2(13) check (sexe in ('male', 'femelle', 'hermaphrodite')),
	typeA varchar2(15),
	pays varchar2(20),
	anNais number(4) check (anNais > 1900),
	noCage number(3) check (noCage > 0),
	constraint pk_LesAnimaux primary key (nomA)
);

create table LesMaladies (
	nomA varchar2(20),
	nomM varchar2(20),
	constraint pk_LesMaladies primary key (nomA, nomM),
	constraint fk_LesMaladies_nomA foreign key (nomA) references LesAnimaux(nomA)
);

create table LesHistoiresAff (
	noCage number(3) check (noCage > 0),
	nomE varchar2(20),
	dateFin date,
	constraint pk_LesHistoiresAff primary key (noCage, nomE, dateFin),
	constraint fk_LesHistoiresAff_nomE foreign key (nomE) references LesEmployes(nomE),
	constraint fk_LesHistoiresAff_noCage foreign key (noCage) references LesCages(noCage)
);

-- Trigger empechant de mettre un animal d'un type different de ceux deja present dans la cage
create or replace trigger trg_1
	before insert
	on LesAnimaux
	for each row
declare
	-- On selectionne les animaux deja present dans la cage
	cursor c1 is select * from LesAnimaux where noCage = :new.noCage;
	resultat LesAnimaux%rowtype;
begin
	open c1;
	fetch c1 into resultat;
	-- Si l'animal a inserer est de type different que celui trouve dans la cage, error
	if (c1%found and resultat.typeA != :new.typeA) then
		raise_application_error(-20011, 'Impossible de mettre un '||:new.typeA ||' dans une cage avec un ' || resultat.typeA );
	end if;
	close c1;
End ;
/

-- Trigger empechant de placer des animaux dans une cage non gardee
create or replace trigger trg_2
	before insert or update
	on LesAnimaux
	for each row
declare
	-- On selectionne tous les gardiens
	cursor c2 is select * from LesGardiens;
	garde boolean := false;
	resultat LesGardiens%rowtype;
begin
	-- On regarde pour tous les gardiens
	for resultat in c2 loop
		-- Si l'un d'eux est affecte a la cage, pas d'erreur
		if (resultat.noCage = :new.noCage) then
			garde := true;
		end if;
	end loop;
	-- Si aucun gardien garde la cage, error
	if (not garde) then
		raise_application_error(-20021, 'Impossible de mettre un animal dans une cage non gardee');
	end if;
End ;
/

-- Trigger empechant d'affecter un employe a la tache de gardien et de responsable (partie gardien)
create or replace trigger trg_3G
	before insert or update
	on LesGardiens
	for each row
declare
	-- On compte le nombre de responsable qui ont le nom du gardien a inserer
	cursor c3G is select count (*) from LesResponsables where nomE=:new.nomE;
	resultat int;
begin
	open c3G;
	fetch c3G into resultat;
	-- S'il y en a un, error
	if (resultat > 0) then
		raise_application_error(-20022, 'Impossible de cumuler les postes de gardien et de responsable');
	end if;
	close c3G;
End ;
/

-- Trigger empechant d'affecter un employe a la tache de gardien et de responsable (partie responsable)
create or replace trigger trg_3R
	before insert or update
	on LesResponsables
	for each row
declare
	-- On compte le nombre de responsable qui ont le nom du responsable a inserer
	cursor c3R is select count (*) from LesGardiens where nomE=:new.nomE;
	resultat int;
begin
	open c3R;
	fetch c3R into resultat;
	-- S'il y en a un, error
	if (resultat > 0) then
		raise_application_error(-20031, 'Impossible de cumuler les postes de gardien et de responsable');
	end if;
	close c3R;
End ;
/

-- Trigger empechant d'affecter un gardien a une cage vide qui a deja un gardien pour le menage
create or replace trigger trg_4G
	after update or insert
	on LesGardiens
declare
	-- On regarde le nombre de fois que plusieurs gardiens gardent une cage vide
	cursor c4 is select count(*) from
	(
		-- On compte le nombre de gardiens affectes a chaque case vide
		select count(*) as nbGardiens 
		from LesGardiens G left outer join LesAnimaux A on G.noCage=A.noCage 
		where A.noCage is null 
		group by (G.noCage)
	)
	where nbGardiens > 1;
	resultat int;
begin
	open c4;
	fetch c4 into resultat;
	if (resultat>0) then
		raise_application_error(-20041, 'Il ne peut pas y avoir plusieurs gardiens pour une cage vide.');
	end if;
	close c4;
End ;
/

-- Trigger empechant d'enlever le dernier animal d'une cage si plusieurs gardiens y sont affectes
create or replace trigger trg_4A
	after update or delete
	on LesAnimaux
declare
	-- Idem que trg_4G
	-- On regarde le nombre de fois que plusieurs gardiens gardent une cage vide
	cursor c4 is select count(*) from
	(
		-- On compte le nombre de gardiens affectes a chaque case vide
		select count(*) as nbGardiens 
		from LesGardiens G left outer join LesAnimaux A on G.noCage=A.noCage 
		where A.noCage is null 
		group by (G.noCage)
	)
	where nbGardiens > 1;
	resultat int;
begin
	open c4;
	fetch c4 into resultat;
	if (resultat>0) then
		raise_application_error(-20042, 'Il ne peut pas y avoir plusieurs gardiens pour une cage vide.');
	end if;
	close c4;
End ;
/

-- Trigger empechant d'enlever le dernier gardien d'une cage non vide
create or replace trigger trg_5
	after update or delete
	on LesGardiens
declare
	-- On compte le nombre de cage non vide qui ne sont pas surveillees
	cursor c5 is select count(*) 
			   from LesGardiens G right outer join LesAnimaux A on G.noCage=A.noCage 
			   where G.noCage is null;
	resultat int;
begin
	open c5;
	fetch c5 into resultat;
	if (resultat!=0) then
		raise_application_error(-20051, 'Impossible d''enlever l''unique gardien d''une cage non vide');
	end if;
	close c5;
End ;
/

-- Trigger empechant d'affecter un responsable a une allee dont toutes les cages sont vides
create or replace trigger trg_6R
	after update or insert
	on LesResponsables
declare
	-- On recupere les animaux par allee (recuperes via LesCages) que l'on ordonne par noAllee
	cursor c6 is select noAllee, nomA 
		from LesCages C natural join LesResponsables R left outer join LesAnimaux A on C.noCage = A.noCage 
		group by noAllee, nomA
		order by noAllee;
	resultat c6%rowtype;
	precedent int;
begin
	open c6;
	fetch c6 into resultat;
	-- precedent correspond au numero de l'allee precedente, 0 au debut car il n'y a pas eu de precedente
	precedent:=0;
	-- Pour chaque resultat de la requete
	while (c6%found) loop
		-- Si dans une entree du resultat une allee ne contient pas d'animaux et que l'entree precedente n'etait pas pour cette allee, alors cette allee ne contient pas d'animaux
		if (resultat.nomA is null and resultat.noAllee!=precedent) then
			raise_application_error(-20061, 'Impossible d''affecter un responsable a une allee dont les cages sont vides');
		end if;
		-- On met a jour precedent avec le numero de la nouvelle allee
		precedent:=resultat.noAllee;
		fetch c6 into resultat;
	end loop;
	close c6;
End ;
/

-- Trigger empechant d'enlever le dernier animal de l'allee
create or replace trigger trg_6A
	after update or delete
	on LesAnimaux
declare
	cursor c6 is select noAllee, nomA 
		from LesCages C natural join LesResponsables R left outer join LesAnimaux A on C.noCage = A.noCage 
		group by noAllee, nomA
		order by noAllee;
	resultat c6%rowtype;
	precedent int;
begin
	-- Idem que trg_6R
	open c6;
	fetch c6 into resultat;
	precedent:=0;
	while (c6%found) loop
		if (resultat.nomA is null and resultat.noAllee!=precedent) then
			raise_application_error(-20062, 'Impossible d''enlever un animal d''une cage si l''allee devient vide');
		end if;
		precedent:=resultat.noAllee;
		fetch c6 into resultat;
	end loop;
	close c6;
End ;
/

-- Trigger ajoutant une entree dans LesHistoireAff quand on change/supprime son affectation a une cage
create or replace trigger trg_7
	after update or delete
	on LesGardiens
	for each row
declare
	d date;
	-- Pour recuperer la date actuelle
	cursor cd is select sysdate from dual;
begin
	open cd;
	fetch cd into d;
	-- On ajoute une entree dans LesHistoiresAff avec son ancienne cage et la date actuelle
	insert into LesHistoiresAff values (:old.noCage, :old.nomE, d);
	close cd;
End ;
/
