package com.example.automotive.controller;

import com.example.automotive.entity.Orders;
import com.example.automotive.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Orders> createOrder(@RequestParam Long partId, @RequestParam int quantity) {
        try {
            Orders order = orderService.createOrder(partId, quantity);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> processBulkOrders(@RequestBody List<Orders> orders) {
        if (orders == null || orders.isEmpty()) {
            return new ResponseEntity<>("Order list is empty or invalid.", HttpStatus.BAD_REQUEST);
        }
        try {
            List<Orders> createdOrders = orderService.processBulkOrders(orders);
            return new ResponseEntity<>(createdOrders, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to process bulk orders: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Orders> completeOrder(@PathVariable Long id) {
        try {
            Orders completedOrder = orderService.completeOrder(id);
            return new ResponseEntity<>(completedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrder(@PathVariable Long id) {
        Orders order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(order);
    }


}