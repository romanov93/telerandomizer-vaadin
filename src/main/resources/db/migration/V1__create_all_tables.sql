CREATE TABLE users
(
    id       SERIAL,
    name     VARCHAR(40) UNIQUE,
    email    VARCHAR(100),
    password VARCHAR(100),
    role     VARCHAR(8),
    PRIMARY KEY (id)
);

CREATE TABLE region
(
    id   SERIAL,
    code INT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE city
(
    id        SERIAL,
    name      VARCHAR(255),
    region_id INT REFERENCES region(id),
    PRIMARY KEY (id)
);

CREATE TABLE city_code
(
    id      SERIAL,
    code    INT,
    city_id INT REFERENCES city(id),
    PRIMARY KEY (id)
);

CREATE TABLE range
(
    id                INT,
    size              INT,
    code              INT,
    first_phone       INT,
    last_phone        INT,
    location_from_csv VARCHAR(255),
    numbers_type      INT,
    city_id           INT REFERENCES city(id),
    region_id         INT REFERENCES  region(id),
    PRIMARY KEY (id)
);

CREATE SEQUENCE range_sequence START WITH 1 INCREMENT 100;

CREATE TABLE phone
(
    id            BIGINT,
    number        BIGINT,
    city_name     VARCHAR(50),
    region_name   VARCHAR(255),
    comment       VARCHAR(50),
    creation_date TIMESTAMP(6),
    number_type   INT,
    status        VARCHAR(16),
    range_id      INT REFERENCES range(id),
    user_id       INT REFERENCES users(id),
    PRIMARY KEY (id)
);

CREATE SEQUENCE phone_sequence START WITH 1 INCREMENT 100;

CREATE TABLE csv_file
(
    id SERIAL,
    name VARCHAR NOT NULL,
    version INT NOT NULL,
    download_date DATE,
    PRIMARY KEY (id)
);

