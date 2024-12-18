package com.example.shipment_model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shipment_model.entity.Shipment;
import com.example.shipment_model.repository.ShipmentRepository;


@Service
public class ShipmentService {

    @Autowired
    ShipmentRepository shipmentRepository;

    public List<Shipment> getAllShipment() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentByTrackNo(String trackNo) {
        // Fetch shipment by track number from the repository
        Shipment shipment = shipmentRepository.findGetShipmentByTrackNo(trackNo);
        return shipment;
    }

    public String deleteById(Integer shipId) {
        Optional<Shipment> shipment = shipmentRepository.findById(shipId);
        if (shipment.isPresent()) {
            shipmentRepository.deleteById(shipId);
            return "The requested shipId got deleted";
        }
        return "Shipment not found with shipId: " + shipId;
    }
}