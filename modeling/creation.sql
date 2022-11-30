CREATE TABLE address (
    id          INTEGER PRIMARY KEY,
    country     VARCHAR2 NOT NULL,
    city        VARCHAR2 NOT NULL,
    district    VARCHAR2 NOT NULL,
    street      VARCHAR2,
    flat_number VARCHAR2,
    post_code   VARCHAR2 NOT NULL
);


CREATE TABLE cleaningcompany (
    nip        CHAR(10 CHAR) PRIMARY KEY,
    email      VARCHAR2 NOT NULL,
    phone      INTEGER NOT NULL,
    address_id INTEGER NOT NULL REFERENCES address(id)
);


CREATE TABLE user (
    login      VARCHAR2 PRIMARY KEY,
    password   VARCHAR2 NOT NULL,
    email      VARCHAR2 NOT NULL,
    phone      VARCHAR2,
    fullname   VARCHAR2 NOT NULL,
    address_id INTEGER REFERENCES address(id)
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
    user_login           VARCHAR2 NOT NULL REFERENCES user(login),
    meeting_localization VARCHAR2 
);


CREATE TABLE trash (
    id                 INTEGER PRIMARY KEY,
    localization       VARCHAR2 NOT NULL,
    creation_date      DATE NOT NULL,
    size               INTEGER,
    crew_name          VARCHAR2,
    cleaningcrew_date  DATE,
    cleaningcrew_login VARCHAR2,
    vehicle_id         INTEGER REFERENCES vehicle(id),
    user_login         VARCHAR2 REFERENCES user(login)
);

ALTER TABLE trash
    ADD CONSTRAINT trash_cleaningcrew_fk FOREIGN KEY ( cleaningcrew_id )
        REFERENCES cleaningcrew ( id );


CREATE TABLE trasharchive (
    id                                INTEGER PRIMARY KEY,
    localization                      VARCHAR2 NOT NULL,
    creation_date                     DATE NOT NULL,
    size                              INTEGER,
    collection_date                   DATE,
    collectingpoint_loc               VARCHAR2 NOT NULL REFERENCES trashcollectingpoint(localization)
);


CREATE TABLE trashcollectingpoint (
    localization    VARCHAR2 PRIMARY KEY,
    bus_empty       CHAR(1) NOT NULL,
    processing_type VARCHAR2 NOT NULL
);


CREATE TABLE trashtype (
    typename                          VARCHAR2 PRIMARY KEY,
    trash_id                          INTEGER REFERENCES trash(id),
    trasharchive_id                   INTEGER REFERENCES trash_archive(id),
    collectingpoint_loc               VARCHAR2 REFERENCES trashcollectingpoint(localization)
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
    company_nip CHAR(10 CHAR) NOT NULL REFERENCES cleaningcompany(nip),
    vehicle_id          INTEGER NOT NULL REFERENCES vehicle(id)
);

ALTER TABLE worker ADD CONSTRAINT worker_pk PRIMARY KEY ( fullname,
                                                          birthdate );
