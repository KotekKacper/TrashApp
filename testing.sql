insert into users (login, password, email, fullname) values ('KG', '1234', 'kg@kg.pl', 'Kacper Garncarek');
exec NewTrash('Poznan2', 'KG');
select * from trash;


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
