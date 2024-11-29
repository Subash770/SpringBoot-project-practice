package com.example.automotive;

import com.example.automotive.controller.OrderController;
import com.example.automotive.entity.Orders;
import com.example.automotive.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        // Arrange
        Long partId = 1L;
        int quantity = 10;
        Orders mockOrder = new Orders(); // Assuming Orders has a no-arg constructor
        when(orderService.createOrder(partId, quantity)).thenReturn(mockOrder);

        // Act
        ResponseEntity<Orders> response = orderController.createOrder(partId, quantity);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).createOrder(partId, quantity);
    }

    @Test
    void processBulkOrders_ShouldReturnCreatedOrders() {
        // Arrange
        List<Orders> mockOrders = new ArrayList<>();
        mockOrders.add(new Orders());
        mockOrders.add(new Orders());
        when(orderService.processBulkOrders(mockOrders)).thenReturn(mockOrders);

        // Act
        ResponseEntity<?> response = orderController.processBulkOrders(mockOrders);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).processBulkOrders(mockOrders);
    }

    @Test
    void processBulkOrders_ShouldReturnBadRequestForEmptyList() {
        // Arrange
        List<Orders> emptyOrderList = new ArrayList<>();

        // Act
        ResponseEntity<?> response = orderController.processBulkOrders(emptyOrderList);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order list is empty or invalid.", response.getBody());
    }

    @Test
    void completeOrder_ShouldReturnCompletedOrder() {
        // Arrange
        Long orderId = 1L;
        Orders mockOrder = new Orders();
        when(orderService.completeOrder(orderId)).thenReturn(mockOrder);

        // Act
        ResponseEntity<Orders> response = orderController.completeOrder(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).completeOrder(orderId);
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        // Arrange
        List<Orders> mockOrders = new ArrayList<>();
        mockOrders.add(new Orders());
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        // Act
        ResponseEntity<List<Orders>> response = orderController.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrder_ShouldReturnOrderIfFound() {
        // Arrange
        Long orderId = 1L;
        Orders mockOrder = new Orders();
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);

        // Act
        ResponseEntity<Orders> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrder_ShouldReturnNotFoundIfOrderDoesNotExist() {
        // Arrange
        Long orderId = 1L;
        when(orderService.getOrderById(orderId)).thenReturn(null);

        // Act
        ResponseEntity<Orders> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}