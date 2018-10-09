CREATE TABLE Tests (
	id serial NOT NULL,
	path VARCHAR(255) NOT NULL UNIQUE,
	price integer NOT NULL,
	max_points integer NOT NULL,
	creator integer NOT NULL,
	enabled BOOLEAN NOT NULL,
	type VARCHAR(255) NOT NULL,
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
	email VARCHAR(255) NOT NULL UNIQUE,
	rank VARCHAR(255) NOT NULL,
	enabled BOOLEAN NOT NULL,
	CONSTRAINT Users_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Testreports (
	id serial NOT NULL,
	description TEXT NOT NULL,
	reporting_user integer NOT NULL,
	test_id integer NOT NULL,
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



CREATE TABLE Testrating (
	id serial NOT NULL,
	voter_id integer NOT NULL,
	test_id integer NOT NULL,
	vote integer NOT NULL,
	CONSTRAINT Testrating_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



ALTER TABLE Tests ADD CONSTRAINT Tests_fk0 FOREIGN KEY (creator) REFERENCES Users(id);

ALTER TABLE Authorities ADD CONSTRAINT Authorities_fk0 FOREIGN KEY (username) REFERENCES Users(username);


ALTER TABLE Testreports ADD CONSTRAINT Testreports_fk0 FOREIGN KEY (reporting_user) REFERENCES Users(id);
ALTER TABLE Testreports ADD CONSTRAINT Testreports_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

ALTER TABLE Userstests ADD CONSTRAINT Userstests_fk0 FOREIGN KEY (user_id) REFERENCES Users(id);
ALTER TABLE Userstests ADD CONSTRAINT Userstests_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);


ALTER TABLE Poolpoints ADD CONSTRAINT Poolpoints_fk0 FOREIGN KEY (voter_id) REFERENCES Users(id);
ALTER TABLE Poolpoints ADD CONSTRAINT Poolpoints_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

ALTER TABLE Testrating ADD CONSTRAINT Testrating_fk0 FOREIGN KEY (voter_id) REFERENCES Users(id);
ALTER TABLE Testrating ADD CONSTRAINT Testrating_fk1 FOREIGN KEY (test_id) REFERENCES Tests(id);

