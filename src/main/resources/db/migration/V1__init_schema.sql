CREATE TABLE zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    area DOUBLE NOT NULL,
    crop_type VARCHAR(255) NOT NULL,
    water_allocation_limit DOUBLE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE crops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    water_per_stage DOUBLE,
    growing_season_days INTEGER,
    optimal_temperature_min DOUBLE,
    optimal_temperature_max DOUBLE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE irrigation_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zone_id BIGINT NOT NULL,
    start_time TIMESTAMP,
    duration INTEGER,
    water_volume DOUBLE,
    recurrence_rule VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES zones(id)
);

CREATE TABLE sensor_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(255) NOT NULL,
    sensor_type VARCHAR(255) NOT NULL,
    value DOUBLE NOT NULL,
    unit VARCHAR(50),
    timestamp TIMESTAMP,
    zone_id BIGINT,
    created_at TIMESTAMP
);

CREATE TABLE devices (
    device_id VARCHAR(255) NOT NULL PRIMARY KEY,
    hardware_model VARCHAR(255),
    firmware_version VARCHAR(100),
    target_firmware_version VARCHAR(100),
    status VARCHAR(50),
    last_heartbeat TIMESTAMP,
    associated_zones TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(255),
    zone_id BIGINT,
    alert_type VARCHAR(255),
    alert_level VARCHAR(50),
    message TEXT,
    acknowledged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    acknowledged_at TIMESTAMP
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP
);

CREATE TABLE schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(50),
    created_at TIMESTAMP
);