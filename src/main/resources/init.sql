ALTER TABLE Tests DROP CONSTRAINT IF EXISTS Tests_fk0;

ALTER TABLE Authorities DROP CONSTRAINT IF EXISTS Authorities_fk0;

ALTER TABLE Testreports DROP CONSTRAINT IF EXISTS Testreports_fk0;

ALTER TABLE Testreports DROP CONSTRAINT IF EXISTS Testreports_fk1;

ALTER TABLE Userstests DROP CONSTRAINT IF EXISTS Userstests_fk0;

ALTER TABLE Userstests DROP CONSTRAINT IF EXISTS Userstests_fk1;

ALTER TABLE Poolpoints DROP CONSTRAINT IF EXISTS Poolpoints_fk0;

ALTER TABLE Poolpoints DROP CONSTRAINT IF EXISTS Poolpoints_fk1;

ALTER TABLE Testratings DROP CONSTRAINT IF EXISTS Testratings_fk0;

ALTER TABLE Testratings DROP CONSTRAINT IF EXISTS Testratings_fk1;

ALTER TABLE News DROP CONSTRAINT IF EXISTS News_fk0;

ALTER TABLE Usertokens DROP CONSTRAINT IF EXISTS Usertokens_fk0;

ALTER TABLE Passwordtokens DROP CONSTRAINT IF EXISTS Passwordtokens_fk0;

ALTER TABLE Deleterequests DROP CONSTRAINT IF EXISTS Deleterequests_fk0;

DROP TABLE IF EXISTS Tests;

DROP TABLE IF EXISTS Authorities;

DROP TABLE IF EXISTS Users;

DROP TABLE IF EXISTS Testreports;

DROP TABLE IF EXISTS Userstests;

DROP TABLE IF EXISTS Payments;

DROP TABLE IF EXISTS Poolpoints;

DROP TABLE IF EXISTS Testratings;

DROP TABLE IF EXISTS News;

DROP TABLE IF EXISTS Usertokens;

DROP TABLE IF EXISTS Passwordtokens;

DROP TABLE IF EXISTS Deleterequests;

CREATE TABLE Tests (
	id serial NOT NULL,
	title VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	path VARCHAR(255) NOT NULL UNIQUE,
	pool_rating integer DEFAULT 0,
	rating NUMERIC(5,2) DEFAULT 0,
	price integer NOT NULL,
	max_points integer NOT NULL,
	creator integer NOT NULL,
	enabled BOOLEAN NOT NULL,
	type VARCHAR(255) NOT NULL,
	live BOOLEAN DEFAULT false,
	CONSTRAINT Tests_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Authorities (
	username VARCHAR(255) NOT NULL UNIQUE,
	authority VARCHAR(255) NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE Users (
	id serial NOT NULL,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(255) UNIQUE,
	rank VARCHAR(255),
	enabled BOOLEAN NOT NULL,
	CONSTRAINT Users_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Testreports (
	id serial NOT NULL,
	description TEXT NOT NULL,
	user_id integer NOT NULL,
	test_id integer NOT NULL,
	solved boolean DEFAULT false,
	CONSTRAINT Testreports_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Userstests (
	id serial NOT NULL,
	user_id integer NOT NULL,
	test_id integer NOT NULL,
	max_points integer NOT NULL,
	actual_points integer NOT NULL,
	percentage integer NOT NULL,
	CONSTRAINT Userstests_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Payments (

) WITH (
  OIDS=FALSE
);



CREATE TABLE Poolpoints (
	id serial NOT NULL,
	voter_id integer NOT NULL,
	test_id integer NOT NULL,
	vote integer NOT NULL,
	CONSTRAINT Poolpoints_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Testratings (
	id serial NOT NULL,
	voter_id integer NOT NULL,
	test_id integer NOT NULL,
	vote integer NOT NULL,
	CONSTRAINT Testratings_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);

CREATE TABLE News (
    id serial NOT NULL,
    title TEXT NOT NULL,
    author integer NOT NULL,
    content TEXT NOT NULL,
    CONSTRAINT News_pg PRIMARY KEY (id)
) WITH (
  OIDS = FALSE
);

CREATE TABLE Usertokens (
	id serial NOT NULL,
    user_id integer NOT NULL,
    token TEXT NOT NULL,
    activated BOOLEAN NOT NULL,
    activation_time DATE NOT NULL,
    CONSTRAINT Usertokens_pg PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);

CREATE TABLE Passwordtokens (
	id serial NOT NULL,
    user_id integer NOT NULL,
    token TEXT NOT NULL,
    expiration_date DATE NOT NULL,
    CONSTRAINT Passwordtokens_pg PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);

CREATE TABLE Deleterequests (
    id serial NOT NULL,
    test_id integer NOT NULL,
    solved boolean DEFAULT FALSE,
    CONSTRAINT Deleterequests_pg PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);


ALTER TABLE Tests ADD CONSTRAINT Tests_fk0 FOREIGN KEY (creator) REFERENCES Users(id);

ALTER TABLE Authorities ADD CONSTRAINT Authorities_fk0 FOREIGN KEY (username) REFERENCES Users(username);


ALTER TABLE Testreports ADD CONSTRAINT Testreports_fk0 FOREIGN KEY (user_id) REFERENCES Users(id);
ALTER TABLE Testreports ADD CONSTRAINT Testreports_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

ALTER TABLE Userstests ADD CONSTRAINT Userstests_fk0 FOREIGN KEY (user_id) REFERENCES Users(id);
ALTER TABLE Userstests ADD CONSTRAINT Userstests_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);


ALTER TABLE Poolpoints ADD CONSTRAINT Poolpoints_fk0 FOREIGN KEY (voter_id) REFERENCES Users(id);
ALTER TABLE Poolpoints ADD CONSTRAINT Poolpoints_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

ALTER TABLE Testratings ADD CONSTRAINT Testratings_fk0 FOREIGN KEY (voter_id) REFERENCES Users(id);
ALTER TABLE Testratings ADD CONSTRAINT Testratings_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

ALTER TABLE News ADD CONSTRAINT News_fk0 FOREIGN KEY (author) REFERENCES Users(id);

ALTER TABLE Usertokens ADD CONSTRAINT Usertokens_fk0 FOREIGN KEY (user_id) REFERENCES Users(id);

ALTER TABLE Passwordtokens ADD CONSTRAINT Passwordtokens_fk0 FOREIGN KEY (user_id) REFERENCES Users(id);

ALTER TABLE Deleterequests ADD CONSTRAINT Deleterequests_fk0 FOREIGN KEY (test_id) REFERENCES Tests(id);

