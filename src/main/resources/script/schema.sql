-- TODO : #4 schema

CREATE TABLE IF NOT EXISTS MEMBERS (
    MNAME    VARCHAR(50)  NOT NULL,
	PWD      VARCHAR(100) NOT NULL,

	PRIMARY KEY(MNAME)
);

CREATE TABLE IF NOT EXISTS AUTHORITIES (
    MNAME     VARCHAR(50)  NOT NULL,
    AUTHORITY VARCHAR(50)  NOT NULL,

    PRIMARY KEY(MNAME)
);

MERGE INTO MEMBERS VALUES ( 'admin',  '12345' );
MERGE INTO MEMBERS VALUES ( 'member', '67890' );
MERGE INTO MEMBERS VALUES ( 'guest', 'abcde'  );

MERGE INTO AUTHORITIES VALUES ( 'admin' , 'ROLE_ADMIN'  );
MERGE INTO AUTHORITIES VALUES ( 'member', 'ROLE_MEMBER' );
MERGE INTO AUTHORITIES VALUES ( 'guest' , 'ROLE_GUEST'  );