CREATE TABLE member(
                       nic VARCHAR(10) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       contact VARCHAR(15) NOT NULL
);

CREATE TABLE book(
                     isbn VARCHAR(25) PRIMARY KEY ,
                     name VARCHAR(100) NOT NULL,
                     author VARCHAR(100) NOT NULL
);

CREATE TABLE issue(
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      member_id VARCHAR(100) NOT NULL,
                      isbn VARCHAR(25) NOT NULL,
                      date DATE NOT NULL,
                      CONSTRAINT fk_member FOREIGN KEY (nic) REFERENCES member(nic),
                      CONSTRAINT fk_book FOREIGN KEY (isbn) REFERENCES book(isbn)
);