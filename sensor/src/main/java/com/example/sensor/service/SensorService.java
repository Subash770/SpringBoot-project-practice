package com.example.sensor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sensor.entity.Sensor;
import com.example.sensor.repository.SensorRepository;

@Service
public class SensorService {

    @Autowired
    SensorRepository sensorRepo;

    // Get all sensors
    public List<Sensor> getAllSensors() {
        List<Sensor> sensors = sensorRepo.findAll();
        if (sensors.isEmpty()) {
            return null;  // Or throw an exception if required
        }
        return sensors;
    }

    // Get sensor by ID
    public Optional<Sensor> getSensorById(int sensorId) {
        return sensorRepo.findById(sensorId);
    }
}
