package com.example.automotive.controller;

import com.example.automotive.entity.Part;
import com.example.automotive.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @PostMapping
    public ResponseEntity<Part> addPart(@RequestBody Part part) {
        Part createdPart = partService.addPart(part);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Part> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        Part updatedPart = partService.updatePartStock(id, quantity);
        return updatedPart != null ? ResponseEntity.ok(updatedPart) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Part>> getLowStockParts() {
        List<Part> lowStockParts = partService.getLowStockParts();
        return ResponseEntity.ok(lowStockParts);
    }

    @GetMapping("/forecast/{id}")
    public ResponseEntity<String> getRestockPrediction(@PathVariable Long id) {
        Integer prediction = partService.predictRestockDate(id);

        if (prediction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Part not found");
        }

        if (prediction == -1) {
            return ResponseEntity.ok("Not enough data to predict restock");
        } else if (prediction == 0) {
            return ResponseEntity.ok("Restock needed immediately");
        } else {
            return ResponseEntity.ok("Estimated restock needed in " + prediction + " days");
        }
    }


    @GetMapping
    public ResponseEntity<List<Part>> getAllParts() {
        List<Part> parts = partService.getAllParts();
        return ResponseEntity.ok(parts);
    }
}