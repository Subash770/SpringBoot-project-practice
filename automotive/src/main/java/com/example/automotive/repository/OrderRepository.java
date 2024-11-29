package com.example.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.automotive.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
