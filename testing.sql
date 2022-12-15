-- For testing

insert into users (login, password, email, fullname) values ('KG', '1234', 'kg@kg.pl', 'Kacper Garncarek');
exec NewTrash('Poznan2', 'KG');
select * from trash;
insert into cleaningcrew (crew_name, meet_date) values ('g1', TIMESTAMP '1997-01-31 09:26:50.12');
insert into cleaningcrew (crew_name, meet_date) values ('g2', TIMESTAMP '1997-01-31 09:26:50.12');
select * from cleaningcrew;
insert into usergroup (user_login, cleaningcrew_id) values ('KG', 1);
insert into usergroup (user_login, cleaningcrew_id) values ('KG', 2);
select archivetrashcount from dual;
select currenttrashcount from dual;


DROP TABLE worker;
DROP TABLE collectingpointtotrashtype;
DROP TABLE trashtotrashtype;
DROP TABLE trashtype;
DROP TABLE trashcollectingpoint;
DROP TABLE image;
DROP TABLE trash;
DROP TABLE vehicle;
DROP TABLE usergroup;
DROP TABLE cleaningcrew;
DROP TABLE usertorole;
DROP TABLE role;
DROP TABLE users;
DROP TABLE cleaningcompany;


CREATE OR REPLACE FUNCTION ShowGroups(
    l_login IN users.login%TYPE
) RETURN cleaningcrew.id%TYPE
IS
    wynik cleaningcrew.id%TYPE;
    crew_id usergroup.cleaningcrew_id%TYPE;
BEGIN
    SELECT cleaningcrew_id INTO crew_id FROM usergroup
    WHERE user_login = l_login;
    
    SELECT crew_name INTO wynik FROM cleaningcrew
    WHERE id = crew_id;
    
    RETURN wynik;
END ShowGroups;
