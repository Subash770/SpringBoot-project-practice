package com.example.sensor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.sensor.entity.Sensor;
import com.example.sensor.service.SensorService;

@RestController
public class SensorController {

    @Autowired
    SensorService sensorService;

    // Endpoint to get all sensors
    @GetMapping(path = "/sensors")
    public ResponseEntity<List<Sensor>> getAllSensorData() {
        List<Sensor> sensors = sensorService.getAllSensors();
        if (sensors != null && !sensors.isEmpty()) {
            return new ResponseEntity<>(sensors, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if no sensors found
    }

    // Endpoint to get a specific sensor by ID
    @GetMapping(path = "/sensors/{sensorId}")
    public ResponseEntity<Sensor> getSensorDataById(@PathVariable int sensorId) {
        Optional<Sensor> sensor = sensorService.getSensorById(sensorId);
        if (sensor.isPresent()) {
            return new ResponseEntity<>(sensor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if sensor not found
    }

    @GetMapping("/")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("Hello World!!!", HttpStatus.OK);
    }
}
