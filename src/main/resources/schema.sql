DROP TABLE FRAMEWORK_VERSIONS IF EXISTS;
DROP TABLE FRAMEWORKS IF EXISTS;

CREATE TABLE FRAMEWORKS (
    ID bigint auto_increment primary key,
    NAME varchar(30) not null,
    HYPE_LEVEL integer not null
);

CREATE TABLE FRAMEWORK_VERSIONS (
    ID bigint auto_increment primary key,
    FRAMEWORK_ID bigint not null,
    VERSION varchar(15) not null,
    DEPRECATION_DATE date not null,
    foreign key (FRAMEWORK_ID) references FRAMEWORKS(ID)
);




