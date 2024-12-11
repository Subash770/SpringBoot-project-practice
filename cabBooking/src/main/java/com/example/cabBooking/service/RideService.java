package com.example.cabBooking.service;

import com.example.cabBooking.entity.Driver;
import com.example.cabBooking.entity.RideDetails;
import com.example.cabBooking.entity.User;
import com.example.cabBooking.repository.DriverRepository;
import com.example.cabBooking.repository.RideDetailsRepository;
import com.example.cabBooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideService {
    private static final double FARE_PER_KM = 25.0; // Fare in INR
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RideDetailsRepository rideDetailsRepository;
    @Autowired
    private UserRepository userRepository;

    // Book a ride and return ride details with fare calculation
    public RideDetails bookRide(Long userId, String startLocation, String endLocation, double distance) {
        // Check if there is an available driver
        List<Driver> availableDrivers = driverRepository.findByAvailabilityTrue();
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No available drivers");
        }

        // Select the first available driver
        Driver driver = availableDrivers.get(0);

        // Calculate the fare
        double fare = calculateFare(distance);

        // Create and save the ride details
        RideDetails rideDetails = new RideDetails();
        rideDetails.setUserId(userId);
        rideDetails.setDriverId(driver.getId());
        rideDetails.setStartLocation(startLocation);
        rideDetails.setEndLocation(endLocation);
        rideDetails.setFare(fare);
        rideDetails.setRideStatus("Ongoing");
        rideDetails.setStartTime(LocalDateTime.now());

        return rideDetailsRepository.save(rideDetails);
    }

    // Method to calculate fare based on distance
    private double calculateFare(double distance) {
        return distance * FARE_PER_KM; // Fare calculation
    }

   public List<RideDetails> getRecentCompletedRides(Long userId) {
    // Retrieve all rides for the user
    List<RideDetails> completedRides = rideDetailsRepository.findByUserId(userId);

    // Filter completed rides that have a non-null endTime and occurred within the last 30 minutes
    completedRides = completedRides.stream()
            .filter(ride -> ride.getEndTime() != null && ride.getEndTime().isAfter(LocalDateTime.now().minusMinutes(30)))
            .collect(Collectors.toList());

    return completedRides;
}


    // End a ride and calculate the total fare
    public RideDetails endRide(Long rideId) {
        // Find the ride by its ID
        Optional<RideDetails> rideDetailsOpt = rideDetailsRepository.findById(rideId);
        if (!rideDetailsOpt.isPresent()) {
            throw new RuntimeException("Ride not found");
        }

        // Update ride status and end time
        RideDetails rideDetails = rideDetailsOpt.get();
        rideDetails.setRideStatus("Completed");
        rideDetails.setEndTime(LocalDateTime.now());

        return rideDetailsRepository.save(rideDetails);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get all drivers
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // Get all ride details
    public List<RideDetails> getAllRideDetails() {
        return rideDetailsRepository.findAll();
    }
}
