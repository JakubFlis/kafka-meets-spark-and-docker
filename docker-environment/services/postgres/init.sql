CREATE DATABASE book_sink;
\c book_sink; 
CREATE TABLE books (
   isbn  integer PRIMARY KEY,
   year  VARCHAR (4),
   title VARCHAR (500),
   ratig integer NOT NULL
);
