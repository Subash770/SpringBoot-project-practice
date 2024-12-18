CREATE DATABASE IF NOT EXISTS employeedb;
USE employeedb;

CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_Name VARCHAR(50) NOT NULL,
    last_Name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL
);

INSERT IGNORE INTO employees (first_Name, last_Name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com'),
('Michael', 'Johnson', 'michael.johnson@example.com'),
('Emily', 'Williams', 'emily.williams@example.com'),
('David', 'Brown', 'david.brown@example.com');
