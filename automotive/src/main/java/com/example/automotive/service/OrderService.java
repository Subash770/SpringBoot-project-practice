package com.example.automotive.service;

import com.example.automotive.entity.Orders;
import com.example.automotive.entity.Part;
import com.example.automotive.repository.OrderRepository;
import com.example.automotive.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PartRepository partRepository;

    @Transactional
    public Orders createOrder(Long partId, int quantity) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found"));
        
        if (part.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        part.setStockQuantity(part.getStockQuantity() - quantity);
        partRepository.save(part);

        Orders order = new Orders();
        order.setPart(part);
        order.setQuantity(quantity);
        order.setStatus(Orders.Status.IN_PROGRESS);
        order.setOrderDate(new Date());
        return orderRepository.save(order);
    }

    @Transactional
    public List<Orders> processBulkOrders(List<Orders> orders) {
        List<Orders> processedOrders = new ArrayList<>();
        
        for (Orders order : orders) {
            Part part = partRepository.findById(order.getPart().getId())
                    .orElseThrow(() -> new RuntimeException("Part not found"));

            if (part.getStockQuantity() >= order.getQuantity()) {
                part.setStockQuantity(part.getStockQuantity() - order.getQuantity());
                partRepository.save(part);

                order.setStatus(Orders.Status.IN_PROGRESS);
                order.setOrderDate(new Date());
                processedOrders.add(orderRepository.save(order));
            }
        }
        
        return processedOrders;
    }

    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }


    @Transactional
    public Orders completeOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Orders.Status.COMPLETED);
        return orderRepository.save(order);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }
}