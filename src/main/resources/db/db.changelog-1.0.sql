CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE GAME_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE USERS (
                       USER_ID NUMBER(19,0) NOT NULL,
                       USERNAME VARCHAR2(255),
                       CREATION_TIME VARCHAR2(255),
                       LAST_PLAYED VARCHAR2(255),
                       CURRENT_GAME_ID NUMBER(19,0),
                       CONSTRAINT pk_users PRIMARY KEY (USER_ID)
);

CREATE TABLE GAMES (
                       GAME_ID NUMBER(19,0) NOT NULL,
                       ROW_SIZE NUMBER(10,0),
                       COL_SIZE NUMBER(10,0),
                       SNAKE_X NUMBER(10,0),
                       SNAKE_Y NUMBER(10,0),
                       APPLE_X NUMBER(10,0),
                       APPLE_Y NUMBER(10,0),
                       SCORE NUMBER(10,0),
                       USER_ID NUMBER(19,0) NOT NULL,
                       CONSTRAINT pk_games PRIMARY KEY (GAME_ID),
                       CONSTRAINT fk_game_user FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);