CREATE DATABASE IF NOT EXISTS sensordb;

use sensordb;

CREATE TABLE IF NOT EXISTS sensor(
    sensor_id INT AUTO_INCREMENT,
    sensor_name varchar(255),
    sensor_type_name varchar(255),
    units_of_measure varchar(255),
    location_name varchar(255),
    PRIMARY KEY (sensor_id)
);

INSERT IGNORE INTO sensor (sensor_name, sensor_type_name, units_of_measure, location_name)
VALUES
    ('Temperature Sensor 1', 'Temperature', 'Celsius', 'Room A'),
    ('Humidity Sensor 1', 'Humidity', 'Percentage', 'Room B'),
    ('Pressure Sensor 1', 'Pressure', 'Pascal', 'Room C');

