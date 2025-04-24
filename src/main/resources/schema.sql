DROP TABLE IF EXISTS tickets;

CREATE TABLE tickets (
    id BIGINT NOT NULL UNIQUE,
    date_time VARCHAR (255) NOT NULL,
    user_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    price DECIMAL (10,2) NOT NULL,
    seat_number VARCHAR (20) NOT NULL
);
