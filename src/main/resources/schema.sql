DROP TABLE  IF EXISTS pdf;


CREATE TABLE pdf(
pdfId integer NOT NULL,
fileName varchar (100) NOT NULL ,
description varchar (255),
loadDate  timestamp default current_timestamp,
fileSize varchar (25) NOT NULL,
content varbinary,
PRIMARY KEY (pdfId)

);
