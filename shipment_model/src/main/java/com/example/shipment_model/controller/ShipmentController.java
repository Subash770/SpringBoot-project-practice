package com.example.shipment_model.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipment_model.entity.Shipment;
import com.example.shipment_model.service.ShipmentService;



@RestController
public class ShipmentController {

    @Autowired
    ShipmentService shipmentService;
    
    @GetMapping(path = "/shipments")
    public ResponseEntity<List<Shipment>> getAllShipmentEntity() {
        List<Shipment> allShipments = shipmentService.getAllShipment();
        
        if (allShipments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allShipments, HttpStatus.OK);
        }
    }
    
    @GetMapping("/shipments/track-ship/{trackNo}")
    public ResponseEntity<Shipment> getShipmentByTrackNo(@PathVariable String trackNo) {
        Shipment shipment = shipmentService.getShipmentByTrackNo(trackNo);
        
        if (shipment != null) {
            return new ResponseEntity<>(shipment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/shipments/{shipId}")
    public ResponseEntity<String> deleteShipmentByShipId(@PathVariable int shipId) {
        String response = shipmentService.deleteById(shipId);
        
        if (response.contains("deleted")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("Hello World!!!", HttpStatus.OK);
    }
}
