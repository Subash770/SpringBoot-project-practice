package com.example.delivery.repository;


import com.example.delivery.entity.DeliveryOrder;
import com.example.delivery.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findByStatus(String status);

}

