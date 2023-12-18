-- users

CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  accountid int DEFAULT NULL,
  PRIMARY KEY (id)
) ;


-- accounts

CREATE TABLE accounts (
  id int NOT NULL AUTO_INCREMENT,
  balance int DEFAULT NULL,
  iban varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


-- transferts

CREATE TABLE transferts (
  id int NOT NULL AUTO_INCREMENT,
  amount int DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  type_of_transfert enum('TO_FRIEND','TO_OWN_CART') DEFAULT NULL,
  recieverid int DEFAULT NULL,
  senderid int DEFAULT NULL,
  PRIMARY KEY (id)
  );



  -- friends

  CREATE TABLE friends (
    user_id int NOT NULL,
    friend_id int NOT NULL

  ) ;
