package com.example.delivery;

import com.example.delivery.controller.DeliveryOrderController;
import com.example.delivery.entity.DeliveryOrder;
import com.example.delivery.service.DeliveryOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@WebMvcTest(DeliveryOrderController.class)
public class DeliveryOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryOrderService deliveryOrderService;

    @Test
    public void testUpdateOrderStatus_Success() throws Exception {
        Long orderId = 1L;
        String status = "DELIVERED";
        DeliveryOrder updatedOrder = new DeliveryOrder();
        updatedOrder.setStatus(status);

        Mockito.when(deliveryOrderService.updateOrderStatus(orderId, status)).thenReturn(updatedOrder);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/delivery-orders/{orderId}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(status))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Delivery Status Updated Successfully"));
    }


    @Test
    public void testUpdateOrderStatus_InvalidStatus() throws Exception {
        Long orderId = 1L;
        String status = "INVALID";
        Mockito.doThrow(new IllegalArgumentException("Invalid status")).when(deliveryOrderService).updateOrderStatus(orderId, status);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/delivery-orders/{orderId}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(status))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid status"));
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() throws Exception {
        Long orderId = 1L;
        String status = "DELIVERED";
        Mockito.doThrow(new NoSuchElementException("Order not found")).when(deliveryOrderService).updateOrderStatus(orderId, status);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/delivery-orders/{orderId}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(status))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCalculateAverageDeliveryDuration_Success() throws Exception {
        Double averageDuration = 30.0;
        Mockito.when(deliveryOrderService.calculateAverageDeliveryDuration()).thenReturn(averageDuration);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-orders/average-delivery-duration"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("30.0"));
    }

    @Test
    public void testCalculateAverageDeliveryDuration_NoContent() throws Exception {
        Mockito.when(deliveryOrderService.calculateAverageDeliveryDuration()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-orders/average-delivery-duration"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetDeliveredOrdersCountForAllCustomers_Success() throws Exception {
        Map<String, Long> result = new HashMap<>();
        result.put("Customer 1", 2L);
        Mockito.when(deliveryOrderService.getDeliveredOrdersCountForAllCustomers()).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-orders/delivered-orders-count-by-customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.['Customer 1']").value(2));
    }

    @Test
    public void testGetDeliveredOrdersCountForAllCustomers_NoContent() throws Exception {
        Mockito.when(deliveryOrderService.getDeliveredOrdersCountForAllCustomers()).thenReturn(Collections.emptyMap());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-orders/delivered-orders-count-by-customer"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
