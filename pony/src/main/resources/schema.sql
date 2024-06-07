


DROP TABLE IF EXISTS MY_ANIMALS;
DROP TABLE IF EXISTS MY_STABLES;
DROP TABLE IF EXISTS ADDRESSES;

CREATE TABLE ADDRESSES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    house_number INT CHECK (house_number > 0),
    place VARCHAR(255) NOT NULL
);

CREATE TABLE MY_STABLES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_animals INT CHECK (max_animals > 0),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id)
);

CREATE TABLE MY_ANIMALS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age >= 1 AND age <= 50),
    stable_id INT,
    FOREIGN KEY (stable_id) REFERENCES MY_STABLES(id)
);

