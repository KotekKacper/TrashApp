CREATE DATABASE TrashApp;
USE TrashApp;


CREATE TABLE cleaningcompany (
     nip        CHAR(10) PRIMARY KEY,
     email      VARCHAR(50) NOT NULL,
     phone      VARCHAR(50) NOT NULL,
     country     VARCHAR(50),
     city        VARCHAR(50),
     district    VARCHAR(50),
     street      VARCHAR(50),
     flat_number VARCHAR(50),
     post_code   VARCHAR(50)
 );


 CREATE TABLE users (
     login      VARCHAR(50) PRIMARY KEY,
     password   VARCHAR(50) NOT NULL,
     email      VARCHAR(50) NOT NULL,
     phone      VARCHAR(50),
     fullname   VARCHAR(50),
     country     VARCHAR(50),
     city        VARCHAR(50),
     district    VARCHAR(50),
     street      VARCHAR(50),
     flat_number VARCHAR(50),
     post_code   VARCHAR(50)
 );


 CREATE TABLE role (
     role_name   VARCHAR(50) PRIMARY KEY
 );


 CREATE TABLE usertorole (
     user_login VARCHAR(50)  NOT NULL,
     role_name VARCHAR(50) NOT NULL,
     FOREIGN KEY (user_login) REFERENCES users(login),
     FOREIGN KEY (role_name) REFERENCES role(role_name)
 );

 ALTER TABLE usertorole ADD PRIMARY KEY ( user_login, role_name );


 CREATE TABLE cleaningcrew (
         id INT AUTO_INCREMENT PRIMARY KEY,
     crew_name            VARCHAR(50) NOT NULL,
     meet_date            TIMESTAMP NOT NULL,
     meeting_localization VARCHAR(50)
 );


 CREATE TABLE usergroup (
     user_login VARCHAR(50)  NOT NULL,
     cleaningcrew_id INT NOT NULL,
     FOREIGN KEY (user_login) REFERENCES users(login),
     FOREIGN KEY (cleaningcrew_id) REFERENCES cleaningcrew(id)
 );

 ALTER TABLE usergroup ADD PRIMARY KEY ( user_login, cleaningcrew_id);




 CREATE TABLE vehicle (
     id           INT AUTO_INCREMENT PRIMARY KEY,
     in_use       CHAR(1) NOT NULL,
     localization VARCHAR(50),
     filling      FLOAT NOT NULL
 );


CREATE TABLE trash (
     id                 INT AUTO_INCREMENT PRIMARY KEY,
     localization       VARCHAR(50) NOT NULL,
     creation_date      TIMESTAMP NOT NULL,
     trash_size         INT,
     vehicle_id         INT,
     user_login_report  VARCHAR(50),
     cleaningcrew_id    INT,
     user_login         VARCHAR(50),
     collection_date    TIMESTAMP,
     FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
     FOREIGN KEY (user_login_report) REFERENCES users(login),
     FOREIGN KEY (cleaningcrew_id) REFERENCES cleaningcrew(id),
     FOREIGN KEY (user_login) REFERENCES users(login)
 );

ALTER TABLE trash
     ADD CHECK ( ( ( vehicle_id IS NOT NULL )
                                             AND ( user_login IS NULL )
                                             AND ( cleaningcrew_id IS NULL ) )
                                           OR ( ( user_login IS NOT NULL )
                                                AND ( vehicle_id IS NULL )
                                                AND ( cleaningcrew_id IS NULL ) )
                                           OR ( ( cleaningcrew_id IS NOT NULL )
                                                AND ( vehicle_id IS NULL )
                                                AND ( user_login IS NULL ) )
											OR ( ( cleaningcrew_id IS NULL )
                                                AND ( vehicle_id IS NULL )
                                                AND ( user_login IS NULL ) ));


 CREATE TABLE image (
     id              INT AUTO_INCREMENT PRIMARY KEY,
     mime_type       VARCHAR(50) NOT NULL,
     content         LONGBLOB NOT NULL,
     trash_id        INT,
     FOREIGN KEY (trash_id) REFERENCES trash(id)
 );


 CREATE TABLE trashcollectingpoint (
     localization    VARCHAR(50) PRIMARY KEY,
     bus_empty       CHAR(1) NOT NULL,
     processing_type VARCHAR(50) NOT NULL,
     trash_id        INT,
     FOREIGN KEY (trash_id) REFERENCES trash(id)
 );


 CREATE TABLE trashtype (
     typename     VARCHAR(50) PRIMARY KEY
 );


 CREATE TABLE trashtotrashtype (
     trash_id INT NOT NULL,
     trashtype_name VARCHAR(50) NOT NULL,
     FOREIGN KEY (trash_id) REFERENCES trash(id),
     FOREIGN KEY (trashtype_name) REFERENCES trashtype(typename)
 );


 CREATE TABLE collectingpointtotrashtype (
     trashcollectingpoint_localization VARCHAR(50) NOT NULL,
     trashtype_name VARCHAR(50) NOT NULL,
     FOREIGN KEY (trashcollectingpoint_localization) REFERENCES trashcollectingpoint(localization),
     FOREIGN KEY (trashtype_name) REFERENCES trashtype(typename)
 );

  CREATE TABLE worker (
       fullname            VARCHAR(50) NOT NULL,
       birthdate           DATE NOT NULL,
       job_start_time      VARCHAR(10) NOT NULL,
       job_end_time        VARCHAR(10) NOT NULL,
       company_nip         CHAR(10) NOT NULL,
       vehicle_id          INT NOT NULL,
       FOREIGN KEY (company_nip) REFERENCES cleaningcompany(nip),
       FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
   );

   ALTER TABLE worker ADD PRIMARY KEY ( fullname, birthdate );


-- funkcje i procedury

DELIMITER $$
CREATE PROCEDURE NewTrash(IN l_localization VARCHAR(50), IN l_user_report VARCHAR(50), IN l_size INT)
BEGIN
    INSERT INTO trash (localization, creation_date, user_login_report, trash_size)
    VALUES (l_localization, NOW(), l_user_report, l_size);
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION CurrentTrashCount()
RETURNS INTEGER
READS SQL DATA
BEGIN
    RETURN (SELECT COUNT(*) FROM trash WHERE collection_date IS NULL);
END;
DELIMITER ;

DELIMITER $$
CREATE FUNCTION ArchiveTrashCount()
RETURNS INTEGER
READS SQL DATA
BEGIN
    RETURN (SELECT COUNT(*) FROM trash WHERE collection_date IS NOT NULL);
END;
DELIMITER ;


-- indeksy

CREATE INDEX users_login_idx ON users(login);
CREATE INDEX users_pwd_idx ON users(password);
CREATE INDEX role_idx ON role(role_name);

CREATE INDEX usertorole_us_login_idx ON usertorole(user_login);
CREATE INDEX usertorole_role_idx ON usertorole(role_name);

CREATE INDEX cleaningcrew_idx ON cleaningcrew(meet_date);

CREATE INDEX usergroup_us_login_idx ON usergroup(user_login);
CREATE INDEX usergroup_cc_id_idx ON usergroup(cleaningcrew_id);

CREATE INDEX vehicle_fill_idx ON vehicle(filling);
CREATE INDEX vehicle_use_idx ON vehicle(in_use);

CREATE INDEX trash_create_date_idx ON trash(creation_date);
CREATE INDEX trash_veh_id_idx ON trash(vehicle_id);
CREATE INDEX trash_us_log_rep_idx ON trash(user_login_report);
CREATE INDEX trash_clean_id_idx ON trash(cleaningcrew_id);
CREATE INDEX trash_us_log_idx ON trash(user_login);
CREATE INDEX trash_collect_date_idx ON trash(collection_date);

CREATE INDEX image_trash_id_idx ON image(trash_id);

CREATE INDEX trashtotrashtype_trash_id_idx ON trashtotrashtype(trash_id);
CREATE INDEX trashtotrashtype_trash_type_name_idx ON trashtotrashtype(trashtype_name);

CREATE INDEX collectpointtotrashtype_tr_col_local_idx ON collectingpointtotrashtype(trashcollectingpoint_localization);
CREATE INDEX collectpointtotrashtype_tr_col_local__typename_idx ON collectingpointtotrashtype(trashtype_name);

CREATE INDEX worker_start_job_idx ON worker(job_start_time);
CREATE INDEX worker_end_job_idx ON worker(job_end_time);
CREATE INDEX worker_company_nip_idx ON worker(company_nip);
CREATE INDEX worker_veh_id_idx ON worker(vehicle_id);


