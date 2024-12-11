package com.example.sensor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sensor.entity.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

}
