package com.example.delivery.service;

import com.example.delivery.entity.Customer;
import com.example.delivery.entity.DeliveryOrder;
import com.example.delivery.repository.CustomerRepository;
import com.example.delivery.repository.DeliveryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public DeliveryOrder updateOrderStatus(Long orderId, String status) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }

        deliveryOrder.setStatus(status);
        return deliveryOrderRepository.save(deliveryOrder);
    }

    private boolean isValidStatus(String status) {
        List<String> validStatuses = Arrays.asList("PENDING", "DELIVERED", "CANCELLED");
        return validStatuses.contains(status);
    }

    public Double calculateAverageDeliveryDuration() {
        List<DeliveryOrder> deliveredOrders = deliveryOrderRepository.findByStatus("DELIVERED");
        
        if (deliveredOrders.isEmpty()) {
            return null;
        }

        long totalDuration = 0;
        for (DeliveryOrder order : deliveredOrders) {
            Duration duration = Duration.between(order.getPickupTime(), order.getDeliveryTime());
            totalDuration += duration.toMinutes();
        }

        return totalDuration / (double) deliveredOrders.size();
    }

    public Map<String, Long> getDeliveredOrdersCountForAllCustomers() {
        List<DeliveryOrder> allOrders = deliveryOrderRepository.findAll();
        Map<Long, Long> customerOrderCount = new HashMap<>();

        for (DeliveryOrder order : allOrders) {
            if ("DELIVERED".equals(order.getStatus())) {
                customerOrderCount.put(order.getCustomerId(),
                        customerOrderCount.getOrDefault(order.getCustomerId(), 0L) + 1);
            }
        }

        Map<String, Long> result = new HashMap<>();
        for (Map.Entry<Long, Long> entry : customerOrderCount.entrySet()) {
            Customer customer = customerRepository.findById(entry.getKey())
                    .orElseThrow(() -> new NoSuchElementException("Customer not found"));
            result.put(customer.getName(), entry.getValue());
        }

        return result;
    }
}
