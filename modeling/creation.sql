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
    user_login VARCHAR2(50)  NOT NULL REFERENCES user(login),
    role_name   INTEGER NOT NULL REFERENCES role(role_name)
);

ALTER TABLE usertorole ADD CONSTRAINT usertorole_pk PRIMARY KEY ( user_login,
                                                                  role_name );


CREATE TABLE cleaningcrew (
	id                 	 INTEGER GENERATED ALWAYS AS IDENTITY,
    crew_name            VARCHAR2(50) NOT NULL,
    meet_date            DATE NOT NULL,
    meeting_localization VARCHAR2(50) 
);


CREATE TABLE usergroup (
    user_login VARCHAR2(50)  NOT NULL REFERENCES user(login),
    cleaningcrew_id INTEGER NOT NULL REFERENCES cleaningcrew(id)
);


CREATE TABLE trash (
    id                 INTEGER  GENERATED ALWAYS AS IDENTITY,
    localization       VARCHAR2(50) NOT NULL,
    creation_date      DATE NOT NULL,
    size               INTEGER,
    vehicle_id         INTEGER REFERENCES vehicle(id),
    user_login_report  VARCHAR2(50) REFERENCES user(login)
    cleaningcrew_id    INTEGER REFERENCES cleaningcrew(id),
    user_login         VARCHAR2(50) REFERENCES user(login),
    collection_date    DATE
);


CREATE TABLE image (
    id              INTEGER  GENERATED ALWAYS AS IDENTITY,
    mime_type       VARCHAR2(50)(100) NOT NULL,
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
    typename     VARCHAR2(50) PRIMARY KEY,
);


CREATE TABLE trashtotrashtype (
    trash_id INTEGER NOT NULL REFERENCES trash(id),
    trashtype_name VARCHAR2(50) NOT NULL REFERENCES trashtype(typename)
);


CREATE TABLE collectingpointtotrashtype (
    trashcollectingpoint_localization VARCHAR2(50) NOT NULL REFERENCES trashcollectingpoint(localization),
    trashtype_name VARCHAR2(50) NOT NULL REFERENCES trashtype(typename)
);


CREATE TABLE vehicle (
    id           INTEGER GENERATED ALWAYS AS IDENTITY,
    in_use       CHAR(1) NOT NULL,
    localization VARCHAR2(50),
    filling      FLOAT NOT NULL
);


CREATE TABLE worker (
    fullname            VARCHAR2(50) NOT NULL,
    birthdate           DATE NOT NULL,
    job_start_time      DATE NOT NULL,
    job_end_time        DATE NOT NULL,
    company_nip         CHAR(10 CHAR) NOT NULL REFERENCES cleaningcompany(nip),
    vehicle_id          INTEGER NOT NULL REFERENCES vehicle(id)
);

ALTER TABLE worker ADD CONSTRAINT worker_pk PRIMARY KEY ( fullname,
                                                          birthdate );
