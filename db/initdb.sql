DROP TABLE IF EXISTS Accounts;

CREATE TABLE accounts (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,
    token VARCHAR(70) NOT NULL UNIQUE,
    esindex VARCHAR(70) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

INSERT INTO accounts (name, token, esindex)
VALUES ("root", "roottoken", "rootes");

INSERT INTO accounts (name, token, esindex)
VALUES ("admin", "admintoken", "admines");