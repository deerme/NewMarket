DROP TABLE IF EXISTS EXECUTION;
DROP TABLE IF EXISTS ORDERINMARKET;

CREATE TABLE ORDERINMARKET(ID INT AUTO_INCREMENT NOT NULL, TYPE VARCHAR(255), QUANTITY INT,PRIMARY KEY(ID));
CREATE TABLE EXECUTION(ID INT AUTO_INCREMENT NOT NULL,QUANTITY VARCHAR(255),ID_ORDER_SELLER INT,ID_ORDER_BUYER INT,FOREIGN KEY( ID_ORDER_SELLER) REFERENCES ORDERINMARKET(ID),FOREIGN KEY( ID_ORDER_BUYER) REFERENCES ORDERINMARKET(ID),PRIMARY KEY(ID));