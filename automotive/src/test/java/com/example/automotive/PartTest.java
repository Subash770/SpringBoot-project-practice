package com.example.automotive;

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
import org.junit.jupiter.api.Test;
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
public class PartTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PartRepository partRepository;

    private Part part;
    private Orders order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        part = new Part();
        part.setId(1L);
        part.setStockQuantity(10);

        order = new Orders();
        order.setId(1L);
        order.setPart(part);
        order.setQuantity(2);
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Long partId = 1L;
        int quantity = 2;

        Part part = new Part();
        part.setId(partId);
        part.setStockQuantity(10); // Enough stock

        when(partRepository.findById(partId)).thenReturn(Optional.of(part));
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> {
            Orders savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L); // Simulate an order ID being set after saving
            return savedOrder;
        });

        // Act
        Orders createdOrder = orderService.createOrder(partId, quantity);

        // Assert
        assertNotNull(createdOrder);
        assertEquals(partId, createdOrder.getPart().getId());
        assertEquals(quantity, createdOrder.getQuantity());
        assertEquals(Orders.Status.IN_PROGRESS, createdOrder.getStatus());

        // Verify interactions
        verify(partRepository).findById(partId);
        verify(partRepository).save(part);
        verify(orderRepository).save(any(Orders.class));

        // Verify stock quantity is updated
        assertEquals(8, part.getStockQuantity());
    }



    @Test
    @Transactional
    public void testCreateOrder_InsufficientStock() {
        part.setStockQuantity(1);
        when(partRepository.findById(1L)).thenReturn(java.util.Optional.of(part));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, 2);
        });

        assertEquals("Insufficient stock", exception.getMessage());
        verify(partRepository, times(0)).save(part);
    }

    @Test
    @Transactional
    public void testProcessBulkOrders_Success() {
        // Arrange
        Orders order1 = new Orders();
        order1.setPart(part); // Set part for the order
        order1.setQuantity(1); // Set quantity for the order

        Orders order2 = new Orders();
        order2.setPart(part); // Set part for the order
        order2.setQuantity(1); // Set quantity for the order

        List<Orders> orders = Arrays.asList(order1, order2); // List of orders to process
        part.setStockQuantity(5); // Ensure sufficient stock
        when(partRepository.findById(part.getId())).thenReturn(java.util.Optional.of(part));

        // Act
        List<Orders> createdOrders = orderService.processBulkOrders(orders);

        // Assert
        assertEquals(2, createdOrders.size()); // Expect two orders to be created successfully
        assertEquals(3, part.getStockQuantity()); // Stock should be reduced by 2
    }

    @Test
    @Transactional
    public void testProcessBulkOrders_Failure() {
        // Arrange
        Orders order1 = new Orders();
        order1.setPart(part); // Set part for the order
        order1.setQuantity(2); // Set quantity for the order

        List<Orders> orders = Arrays.asList(order1);
        part.setStockQuantity(1); // Set insufficient stock
        when(partRepository.findById(part.getId())).thenReturn(java.util.Optional.of(part));

        // Act
        List<Orders> createdOrders = orderService.processBulkOrders(orders);

        // Assert
        assertEquals(0, createdOrders.size()); // Expect no orders to be created
        assertEquals(1, part.getStockQuantity()); // Stock should remain unchanged
    }

    @Test
    @Transactional
    public void testCompleteOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        Orders completedOrder = orderService.completeOrder(1L);

        assertEquals(Orders.Status.COMPLETED, completedOrder.getStatus());
        verify(orderRepository, times(1)).save(completedOrder);
    }

    @Test
    @Transactional
    public void testCompleteOrder_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.completeOrder(1L);
        });

        assertEquals("Order not found", exception.getMessage());
    }
}