CREATE TABLE IF NOT EXISTS drones (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    name            VARCHAR(64) NOT NULL,
    model           VARCHAR(64) NOT NULL,
    serial_no       VARCHAR(64) NOT NULL UNIQUE,

    max_speed_kmh   INTEGER NOT NULL,
    flight_time_min INTEGER NOT NULL,
    payload_kg      DECIMAL(10,2) NOT NULL,
    battery_mah     INTEGER NOT NULL,
    ai_score        INTEGER NOT NULL,

    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL
);

