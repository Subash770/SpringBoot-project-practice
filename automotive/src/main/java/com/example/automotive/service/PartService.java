package com.example.automotive.service;

import com.example.automotive.entity.Part;
import com.example.automotive.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    public Part addPart(Part part) {
        return partRepository.save(part);
    }

    @Transactional
    public synchronized Part updatePartStock(Long partId, int quantity) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        if (quantity < 0 && Math.abs(quantity) > part.getStockQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        part.setStockQuantity(part.getStockQuantity() + quantity);
        return partRepository.save(part);
    }

    public List<Part> getLowStockParts() {
        return partRepository.findByStockQuantityLessThan(0);
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    public Integer predictRestockDate(Long partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        int bufferStock = part.getMinStockLevel();
        int currentStock = part.getStockQuantity();
        double averageDailyConsumption = 5.0; // Hardcoded as per requirements

        if (currentStock <= bufferStock) {
            return 0; // Restock needed immediately
        }
        
        return (int) Math.ceil((currentStock - bufferStock) / averageDailyConsumption);
    }
}