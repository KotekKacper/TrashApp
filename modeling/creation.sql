CREATE TABLE cleaningcompany (
    nip        CHAR(10 CHAR) PRIMARY KEY,
    email      VARCHAR2(50) NOT NULL,
    phone      INTEGER NOT NULL,
    country     VARCHAR2(50),
    city        VARCHAR2(50),
    district    VARCHAR2(50),
    street      VARCHAR2(50),
    flat_number VARCHAR2(50),
    post_code   VARCHAR2(50)
);


CREATE TABLE users (
    login      VARCHAR2(50) PRIMARY KEY,
    password   VARCHAR2(50) NOT NULL,
    email      VARCHAR2(50) NOT NULL,
    phone      VARCHAR2(50),
    fullname   VARCHAR2(50) NOT NULL,
    country     VARCHAR2(50),
    city        VARCHAR2(50),
    district    VARCHAR2(50),
    street      VARCHAR2(50),
    flat_number VARCHAR2(50),
    post_code   VARCHAR2(50)
);


CREATE TABLE role (
    role_name   VARCHAR2(50) PRIMARY KEY
);


CREATE TABLE usertorole (
    user_login VARCHAR2(50)  NOT NULL REFERENCES users(login),
    role_name VARCHAR2(50) NOT NULL REFERENCES role(role_name)
);

ALTER TABLE usertorole ADD CONSTRAINT usertorole_pk PRIMARY KEY ( user_login,
                                                                  role_name );


CREATE TABLE cleaningcrew (
	id                 	 INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    crew_name            VARCHAR2(50) NOT NULL,
    meet_date            TIMESTAMP NOT NULL,
    meeting_localization VARCHAR2(50) 
);


CREATE TABLE usergroup (
    user_login VARCHAR2(50)  NOT NULL REFERENCES users(login),
    cleaningcrew_id INTEGER NOT NULL REFERENCES cleaningcrew(id)
);


ALTER TABLE usergroup ADD CONSTRAINT usergroup_pk PRIMARY KEY ( user_login,
                                                                  cleaningcrew_id);




CREATE TABLE vehicle (
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    in_use       CHAR(1) NOT NULL,
    localization VARCHAR2(50),
    filling      FLOAT NOT NULL
);


CREATE TABLE trash (
    id                 INTEGER  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    localization       VARCHAR2(50) NOT NULL,
    creation_date      TIMESTAMP NOT NULL,
    trash_size         INTEGER,
    vehicle_id         INTEGER REFERENCES vehicle(id),
    user_login_report  VARCHAR2(50) REFERENCES users(login),
    cleaningcrew_id    INTEGER REFERENCES cleaningcrew(id),
    user_login         VARCHAR2(50) REFERENCES users(login),
    collection_date    TIMESTAMP
);

-- XOR dla user_login, vehicle_id i cleaningcrew_id
ALTER TABLE trash
    ADD CONSTRAINT reporttrasharc CHECK ( ( ( vehicle_id IS NOT NULL )
                                            AND ( user_login IS NULL )
                                            AND ( cleaningcrew_id IS NULL ) )
                                          OR ( ( user_login IS NOT NULL )
                                               AND ( vehicle_id IS NULL )
                                               AND ( cleaningcrew_id IS NULL ) )
                                          OR ( ( cleaningcrew_id IS NOT NULL )
                                               AND ( vehicle_id IS NULL )
                                               AND ( user_login IS NULL ) ) );


CREATE TABLE image (
    id              INTEGER  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mime_type       VARCHAR2(50) NOT NULL,
    content         BLOB NOT NULL,
    trash_id        INTEGER REFERENCES trash(id)
);


CREATE TABLE trashcollectingpoint (
    localization    VARCHAR2(50) PRIMARY KEY,
    bus_empty       CHAR(1) NOT NULL,
    processing_type VARCHAR2(50) NOT NULL,
    trash_id        INTEGER REFERENCES trash(id)
);


CREATE TABLE trashtype (
    typename     VARCHAR2(50) PRIMARY KEY
);


CREATE TABLE trashtotrashtype (
    trash_id INTEGER NOT NULL REFERENCES trash(id),
    trashtype_name VARCHAR2(50) NOT NULL REFERENCES trashtype(typename)
);


CREATE TABLE collectingpointtotrashtype (
    trashcollectingpoint_localization VARCHAR2(50) NOT NULL REFERENCES trashcollectingpoint(localization),
    trashtype_name VARCHAR2(50) NOT NULL REFERENCES trashtype(typename)
);



CREATE TABLE worker (
    fullname            VARCHAR2(50) NOT NULL,
    birthdate           DATE NOT NULL,
    job_start_time      VARCHAR(10) NOT NULL,
    job_end_time        VARCHAR(10) NOT NULL,
    company_nip         CHAR(10 CHAR) NOT NULL REFERENCES cleaningcompany(nip),
    vehicle_id          INTEGER NOT NULL REFERENCES vehicle(id)
);

ALTER TABLE worker ADD CONSTRAINT worker_pk PRIMARY KEY ( fullname,
                                                          birthdate );



-- Procedury i funkcje



CREATE OR REPLACE PROCEDURE NewTrash(
    l_localization IN trash.localization%TYPE,
    l_user_report IN trash.user_login_report%TYPE,
    l_size IN trash.trash_size%TYPE := 0
) IS
BEGIN
    INSERT INTO trash (localization, creation_date,
                        user_login_report, trash_size) VALUES
    (l_localization, CURRENT_TIMESTAMP, l_user_report, l_size);
END NewTrash;


CREATE OR REPLACE FUNCTION CurrentTrashCount
 RETURN INTEGER
IS
    wynik INTEGER;
BEGIN
    
    SELECT count(*)
    INTO wynik
    FROM trash
    WHERE collection_date IS NULL;
    
    RETURN wynik;
END CurrentTrashCount;


CREATE OR REPLACE FUNCTION ArchiveTrashCount
 RETURN INTEGER
IS
    wynik INTEGER;
BEGIN
    
    SELECT count(*)
    INTO wynik
    FROM trash
    WHERE collection_date IS NOT NULL;
    
    RETURN wynik;
END ArchiveTrashCount;

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

