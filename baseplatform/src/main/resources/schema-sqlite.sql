-- SQLite schema for drone management system

-- Drop existing table if exists
DROP TABLE IF EXISTS drone;

-- Create drone table
CREATE TABLE drone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    
    -- Basic fields (manual input)
    name TEXT NOT NULL,
    model TEXT NOT NULL,
    serial_no TEXT NOT NULL UNIQUE,
    
    -- AI generated fields
    max_speed_kmh INTEGER,
    flight_time_min INTEGER,
    payload_kg REAL,
    battery_mah INTEGER,
    ai_score INTEGER,
    
    -- System fields
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- Create indexes
CREATE INDEX idx_drone_name ON drone(name);
CREATE INDEX idx_drone_model ON drone(model);
CREATE INDEX idx_drone_serial_no ON drone(serial_no);
CREATE INDEX idx_drone_created_at ON drone(created_at);

-- Insert test data
INSERT INTO drone (name, model, serial_no, max_speed_kmh, flight_time_min, payload_kg, battery_mah, ai_score) VALUES
('天鹰一号', 'TY-001', 'SN20240001', 85, 120, 15.5, 12000, 88),
('雷鸟二号', 'LB-002', 'SN20240002', 95, 90, 8.2, 8500, 82),
('猎鹰三号', 'LY-003', 'SN20240003', 75, 150, 20.0, 15000, 92);
