package com.example.cabBooking;

import com.example.cabBooking.entity.Driver;
import com.example.cabBooking.entity.RideDetails;
import com.example.cabBooking.repository.DriverRepository;
import com.example.cabBooking.repository.RideDetailsRepository;
import com.example.cabBooking.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.cabBooking")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class AppTest {
    @InjectMocks
    private RideService rideService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private RideDetailsRepository rideDetailsRepository;

    private Driver driver;
    private RideDetails rideDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driver.setAvailability(true);

        rideDetails = new RideDetails();
        rideDetails.setId(1L);
        rideDetails.setUserId(1L);
        rideDetails.setDriverId(driver.getId());
        rideDetails.setStartLocation("Start Location");
        rideDetails.setEndLocation("End Location");
        rideDetails.setFare(100.0);
        rideDetails.setRideStatus("Ongoing");
        rideDetails.setStartTime(LocalDateTime.now());
    }

    @Test
    void testBookRide_Success_WithFareCalculation() {
        // Arrange
        when(driverRepository.findByAvailabilityTrue()).thenReturn(Collections.singletonList(driver));
        // Remove hardcoding of fare in rideDetails
        when(rideDetailsRepository.save(any(RideDetails.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        double distance = 10.0; // Mock the distance
        double expectedFare = distance * 25.0; // Fare per km is 25.0 as per the service
        RideDetails result = rideService.bookRide(1L, "Start Location", "End Location", distance);

        // Assert
        assertNotNull(result);
        assertEquals("Start Location", result.getStartLocation());
        assertEquals("End Location", result.getEndLocation());
        assertEquals(expectedFare, result.getFare()); // Now this should be 250.0
        assertEquals("Ongoing", result.getRideStatus());
        assertEquals(driver.getId(), result.getDriverId());
    }

    @Test
    void testBookRide_NoAvailableDrivers() {
        // Arrange
        when(driverRepository.findByAvailabilityTrue()).thenReturn(Collections.emptyList());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                rideService.bookRide(1L, "Start Location", "End Location", 10.0));
        assertEquals("No available drivers", exception.getMessage());
    }

    @Test
    void testGetRecentCompletedRides_Success() {
        // Arrange
        rideDetails.setEndTime(LocalDateTime.now().minusMinutes(10));
        when(rideDetailsRepository.findByUserId(1L)).thenReturn(Collections.singletonList(rideDetails));

        // Act
        List<RideDetails> result = rideService.getRecentCompletedRides(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rideDetails, result.get(0));
    }

    @Test
    void testGetRecentCompletedRides_NoCompletedRides() {
        // Arrange
        rideDetails.setEndTime(null);
        when(rideDetailsRepository.findByUserId(1L)).thenReturn(Collections.singletonList(rideDetails));

        // Act
        List<RideDetails> result = rideService.getRecentCompletedRides(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEndRide_Success() {
        // Arrange
        rideDetails.setEndTime(null);
        when(rideDetailsRepository.findById(1L)).thenReturn(Optional.of(rideDetails));
        when(rideDetailsRepository.save(any(RideDetails.class))).thenReturn(rideDetails);

        // Act
        RideDetails result = rideService.endRide(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Completed", result.getRideStatus());
        assertNotNull(result.getEndTime());
    }

    @Test
    void testEndRide_RideNotFound() {
        // Arrange
        when(rideDetailsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                rideService.endRide(1L));
        assertEquals("Ride not found", exception.getMessage());
    }
}
