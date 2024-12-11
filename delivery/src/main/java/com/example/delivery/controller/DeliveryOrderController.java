package com.example.delivery.controller;

import com.example.delivery.entity.DeliveryOrder;
import com.example.delivery.service.DeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/delivery-orders")
public class DeliveryOrderController {

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestBody String status) {
        try {
            DeliveryOrder updatedOrder = deliveryOrderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Delivery Status Updated Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Order not found");
        }
    }

    @GetMapping("/average-delivery-duration")
    public ResponseEntity<?> calculateAverageDeliveryDuration() {
        Double averageDuration = deliveryOrderService.calculateAverageDeliveryDuration();
        if (averageDuration == null) {
            return ResponseEntity.status(404).body("No content");
        }
        return ResponseEntity.ok(averageDuration);
    }

    @GetMapping("/delivered-orders-count-by-customer")
    public ResponseEntity<?> getDeliveredOrdersCountForAllCustomers() {
        Map<String, Long> deliveredOrdersCount = deliveryOrderService.getDeliveredOrdersCountForAllCustomers();
        if (deliveredOrdersCount.isEmpty()) {
            return ResponseEntity.status(404).body("No content");
        }
        return ResponseEntity.ok(deliveredOrdersCount);
    }
}
