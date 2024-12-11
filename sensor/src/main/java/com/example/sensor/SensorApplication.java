package com.example.sensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.sensor.entity.Sensor;
import com.example.sensor.repository.SensorRepository;


@SpringBootApplication
public class SensorApplication {

	public static void main(String[] args) {
		
        SpringApplication.run(SensorApplication.class, args);		
		

	}

}
