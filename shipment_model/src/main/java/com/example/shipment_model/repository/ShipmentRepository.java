package com.example.shipment_model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.shipment_model.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>{


    @Query("SELECT s from Shipment s where s.trackNo = :trackNo")
    public Shipment findGetShipmentByTrackNo(@Param("trackNo") String trackNo);

}
