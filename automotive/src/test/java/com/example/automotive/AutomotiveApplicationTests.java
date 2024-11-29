package com.example.automotive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.automotive.controller.OrderController;
import com.example.automotive.controller.PartController;
import com.example.automotive.entity.Orders;
import com.example.automotive.entity.Part;
import com.example.automotive.repository.OrderRepository;
import com.example.automotive.repository.PartRepository;
import com.example.automotive.service.OrderService;
import com.example.automotive.service.PartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.automotiveInventory")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class AutomotiveApplicationTests {
    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdatePartStock_Success() {
        Part part = new Part();
        part.setId(1L);
        part.setName("Brake Pad");
        part.setStockQuantity(100);

        when(partRepository.findById(1L)).thenReturn(Optional.of(part));
        when(partRepository.save(part)).thenReturn(part);

        Part updatedPart = partService.updatePartStock(1L, 50);

        assertNotNull(updatedPart);
        assertEquals(150, updatedPart.getStockQuantity());
        verify(partRepository, times(1)).save(part);
    }

    @Test
    void testUpdatePartStock_InsufficientStock() {
        Part part = new Part();
        part.setId(1L);
        part.setName("Brake Pad");
        part.setStockQuantity(10);

        when(partRepository.findById(1L)).thenReturn(Optional.of(part));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            partService.updatePartStock(1L, -20);
        });

        assertEquals("Insufficient stock", thrown.getMessage());
    }

    @Test
    void testUpdatePartStock_PartNotFound() {
        when(partRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            partService.updatePartStock(1L, 50);
        });

        assertEquals("Part not found", thrown.getMessage());
    }

    @Test
    void testGetLowStockParts() {
        Part part1 = new Part();
        part1.setId(1L);
        part1.setName("Brake Pad");
        part1.setStockQuantity(5);
        part1.setMinStockLevel(10);

        Part part2 = new Part();
        part2.setId(2L);
        part2.setName("Oil Filter");
        part2.setStockQuantity(-1);
        part2.setMinStockLevel(5);

        List<Part> lowStockParts = Arrays.asList(part2);

        when(partRepository.findByStockQuantityLessThan(0)).thenReturn(lowStockParts);

        List<Part> result = partService.getLowStockParts();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Oil Filter", result.get(0).getName());
    }



    @Test
    void testPredictRestockDate_Normal() {
        // Scenario: Current stock is sufficient, and average daily consumption is 5.0
        Part part = new Part();
        part.setId(1L);
        part.setStockQuantity(100); // Current stock
        part.setMinStockLevel(10);

        when(partRepository.findById(1L)).thenReturn(Optional.of(part));

        // Call the method
        Integer daysUntilRestock = partService.predictRestockDate(1L);

        // Check the result
        assertEquals(18, daysUntilRestock); // (100 - 10) / 5.0 = 18 days
    }

    @Test
    void testPredictRestockDate_ImmediateRestockNeeded() {
        // Scenario: Current stock is less than or equal to buffer stock
        Part part = new Part();
        part.setId(2L);
        part.setStockQuantity(5);  // Current stock
        part.setMinStockLevel(10);

        when(partRepository.findById(2L)).thenReturn(Optional.of(part));

        // Call the method
        Integer daysUntilRestock = partService.predictRestockDate(2L);

        // Check the result
        assertEquals(0, daysUntilRestock); // Restock needed immediately
    }

    @Test
    void testPredictRestockDate_NoConsumptionData() {
        // Scenario: This case won't actually occur with the current logic since averageDailyConsumption is hardcoded to 5.0
        // But if you wanted to simulate this, you'd adjust the method to take the averageDailyConsumption as a parameter
        Part part = new Part();
        part.setId(3L);
        part.setStockQuantity(50);  // Current stock
        part.setMinStockLevel(10);

        when(partRepository.findById(3L)).thenReturn(Optional.of(part));

        // Call the method
        Integer daysUntilRestock = partService.predictRestockDate(3L);

        // Check the result
        assertEquals(8, daysUntilRestock); // (50 - 10) / 5.0 = 8 days
    }

    @Test
    void testPredictRestockDate_NonExistentPart() {
        // Scenario: Part does not exist
        when(partRepository.findById(4L)).thenReturn(Optional.empty());

        // Call the method and expect an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            partService.predictRestockDate(4L);
        });

        // Check the exception message
        assertEquals("Part not found", exception.getMessage());
    }
}