CREATE TABLE cleaningcompany (
    nip        CHAR(10 CHAR) PRIMARY KEY,
    email      VARCHAR2 NOT NULL,
    phone      INTEGER NOT NULL,
    country     VARCHAR2,
    city        VARCHAR2,
    district    VARCHAR2,
    street      VARCHAR2,
    flat_number VARCHAR2,
    post_code   VARCHAR2
);


CREATE TABLE user (
    login      VARCHAR2 PRIMARY KEY,
    password   VARCHAR2 NOT NULL,
    email      VARCHAR2 NOT NULL,
    phone      VARCHAR2,
    fullname   VARCHAR2 NOT NULL,
    country     VARCHAR2,
    city        VARCHAR2,
    district    VARCHAR2,
    street      VARCHAR2,
    flat_number VARCHAR2,
    post_code   VARCHAR2
);


CREATE TABLE role (
    role_name   VARCHAR2 PRIMARY KEY
);


CREATE TABLE usertorole (
    user_login VARCHAR2  NOT NULL REFERENCES user(login),
    role_name   INTEGER NOT NULL REFERENCES role(role_name)
);

ALTER TABLE usertorole ADD CONSTRAINT usertorole_pk PRIMARY KEY ( user_login,
                                                                  role_name );


CREATE TABLE cleaningcrew (
	id                 	 INTEGER PRIMARY KEY,
    crew_name            VARCHAR2 NOT NULL,
    meet_date            DATE NOT NULL,
    meeting_localization VARCHAR2 
);


CREATE TABLE usergroup (
    user_login VARCHAR2  NOT NULL REFERENCES user(login),
    cleaningcrew_id INTEGER NOT NULL REFERENCES cleaningcrew(id)
);


CREATE TABLE trash (
    id                 INTEGER PRIMARY KEY,
    localization       VARCHAR2 NOT NULL,
    creation_date      DATE NOT NULL,
    size               INTEGER,
    vehicle_id         INTEGER REFERENCES vehicle(id),
    user_login_report  VARCHAR2 REFERENCES user(login)
    cleaningcrew_id    INTEGER REFERENCES cleaningcrew(id),
    user_login         VARCHAR2 REFERENCES user(login),
    collection_date    DATE
);


CREATE TABLE image (
    id              INTEGER PRIMARY KEY,
    mime_type       VARCHAR2(100) NOT NULL,
    content         BLOB NOT NULL,
    trash_id        INTEGER REFERENCES trash(id)
);


CREATE TABLE trashcollectingpoint (
    localization    VARCHAR2 PRIMARY KEY,
    bus_empty       CHAR(1) NOT NULL,
    processing_type VARCHAR2 NOT NULL,
    trash_id        INTEGER REFERENCES trash(id)
);


CREATE TABLE trashtype (
    typename     VARCHAR2 PRIMARY KEY,
);


CREATE TABLE trashtotrashtype (
    trash_id INTEGER NOT NULL REFERENCES trash(id),
    trashtype_name VARCHAR2 NOT NULL REFERENCES trashtype(typename)
);


CREATE TABLE collectingpointtotrashtype (
    trashcollectingpoint_localization VARCHAR2 NOT NULL REFERENCES trashcollectingpoint(localization),
    trashtype_name VARCHAR2 NOT NULL REFERENCES trashtype(typename)
);


CREATE TABLE vehicle (
    id           INTEGER PRIMARY KEY,
    in_use       CHAR(1) NOT NULL,
    localization VARCHAR2,
    filling      FLOAT NOT NULL
);


CREATE TABLE worker (
    fullname            VARCHAR2 NOT NULL,
    birthdate           DATE NOT NULL,
    job_start_time      DATE NOT NULL,
    job_end_time        DATE NOT NULL,
    company_nip         CHAR(10 CHAR) NOT NULL REFERENCES cleaningcompany(nip),
    vehicle_id          INTEGER NOT NULL REFERENCES vehicle(id)
);

ALTER TABLE worker ADD CONSTRAINT worker_pk PRIMARY KEY ( fullname,
                                                          birthdate );
