package com.example.delivery;

import com.example.delivery.entity.Customer;
import com.example.delivery.entity.DeliveryOrder;
import com.example.delivery.repository.CustomerRepository;
import com.example.delivery.repository.DeliveryOrderRepository;
import com.example.delivery.service.DeliveryOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryOrderServiceTest {

    @Mock
    private DeliveryOrderRepository deliveryOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeliveryOrderService deliveryOrderService;

    @Test
    public void testUpdateOrderStatus_Success() {
        // Mock data
        Long orderId = 1L;
        String status = "DELIVERED";
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderId(orderId);

        // Mock repository behavior
        Mockito.when(deliveryOrderRepository.findById(orderId)).thenReturn(Optional.of(deliveryOrder));
        Mockito.when(deliveryOrderRepository.save(deliveryOrder)).thenReturn(deliveryOrder);

        // Test service method
        DeliveryOrder updatedOrder = deliveryOrderService.updateOrderStatus(orderId, status);

        // Assertions
        assertEquals(status, updatedOrder.getStatus());
        Mockito.verify(deliveryOrderRepository, Mockito.times(1)).findById(orderId);
        Mockito.verify(deliveryOrderRepository, Mockito.times(1)).save(deliveryOrder);
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() {
        Long orderId = 1L;
        String status = "DELIVERED";

        Mockito.when(deliveryOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> deliveryOrderService.updateOrderStatus(orderId, status));

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testCalculateAverageDeliveryDuration() {
        // Mock data
        DeliveryOrder order1 = new DeliveryOrder();
        order1.setPickupTime(LocalDateTime.now().minusMinutes(30));
        order1.setDeliveryTime(LocalDateTime.now());

        DeliveryOrder order2 = new DeliveryOrder();
        order2.setPickupTime(LocalDateTime.now().minusMinutes(45));
        order2.setDeliveryTime(LocalDateTime.now().minusMinutes(15));

        List<DeliveryOrder> deliveredOrders = Arrays.asList(order1, order2);

        // Mock repository behavior
        Mockito.when(deliveryOrderRepository.findByStatus("DELIVERED")).thenReturn(deliveredOrders);

        // Test service method
        Double averageDuration = deliveryOrderService.calculateAverageDeliveryDuration();

        // Assertion
        assertEquals(Double.valueOf(30.0), averageDuration); // Assuming exact calculations for simplicity
    }

    @Test
    public void testCalculateAverageDeliveryDuration_NoDeliveredOrders() {
        // Mock repository behavior
        Mockito.when(deliveryOrderRepository.findByStatus("DELIVERED")).thenReturn(Collections.emptyList());

        // Test service method
        assertNull(deliveryOrderService.calculateAverageDeliveryDuration());
    }

    @Test
    public void testGetDeliveredOrdersCountForAllCustomers() {
        // Mock data
        DeliveryOrder order1 = new DeliveryOrder();
        order1.setCustomerId(1L);
        order1.setStatus("DELIVERED");

        DeliveryOrder order2 = new DeliveryOrder();
        order2.setCustomerId(2L);
        order2.setStatus("DELIVERED");

        DeliveryOrder order3 = new DeliveryOrder();
        order3.setCustomerId(1L);
        order3.setStatus("DELIVERED");

        List<DeliveryOrder> allOrders = Arrays.asList(order1, order2, order3);

        // Mock repository behavior
        Mockito.when(deliveryOrderRepository.findAll()).thenReturn(allOrders);

        // Mock customer repository behavior
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setName("Customer 1");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setName("Customer 2");

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        Mockito.when(customerRepository.findById(2L)).thenReturn(Optional.of(customer2));

        // Test service method
        Map<String, Long> result = deliveryOrderService.getDeliveredOrdersCountForAllCustomers();

        // Assertions
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(2), result.get("Customer 1"));
        assertEquals(Long.valueOf(1), result.get("Customer 2"));
    }



    @Test
    public void testGetDeliveredOrdersCountForAllCustomers_NoDeliveredOrders() {
        // Mock repository behavior
        Mockito.when(deliveryOrderRepository.findAll()).thenReturn(Collections.emptyList());

        // Test service method
        Map<String, Long> result = deliveryOrderService.getDeliveredOrdersCountForAllCustomers();

        // Assertions
        assertTrue(result.isEmpty());
    }
}
