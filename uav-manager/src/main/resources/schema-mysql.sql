CREATE TABLE IF NOT EXISTS drones (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(64) NOT NULL,
    model           VARCHAR(64) NOT NULL,
    serial_no       VARCHAR(64) NOT NULL UNIQUE,

    max_speed_kmh   INT NOT NULL,
    flight_time_min INT NOT NULL,
    payload_kg      DECIMAL(10,2) NOT NULL,
    battery_mah     INT NOT NULL,
    ai_score        INT NOT NULL,

    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL
);

