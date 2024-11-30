package com.example.demo.service;

import com.example.demo.model.address.ItemEntity;
import com.example.demo.model.availability.ItemNumberPairEntity;
import com.example.demo.model.availability.WarehouseAvailabilityEnitity;
import com.example.demo.repository.ItemNumberPairRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final ItemRepository itemRepository;
    private final ItemNumberPairRepository itemNumberPairRepository;

    public void seedWarehouse() {
        if (warehouseRepository.count() == 0) {
            WarehouseAvailabilityEnitity warehouseAvailabilityEnitity = new WarehouseAvailabilityEnitity();
            warehouseAvailabilityEnitity.setLines(new ArrayList<>());
            warehouseRepository.save(warehouseAvailabilityEnitity);

        }
    }

    @Transactional
    public boolean setLine(Long itemId, int quantity) {
        Optional<ItemEntity> itemEntity = itemRepository.findById(itemId);
        if (itemEntity.isEmpty()) {
            log.error("Item with id {} not found", itemId);
            return false;
        }
        if (quantity < 0) {
            log.error("Quantity cannot be negative");
            return false;
        }
        if (warehouseRepository.count() == 0) {
            log.error("Warehouse not seeded");
            return false;
        }
        WarehouseAvailabilityEnitity warehouse = warehouseRepository.findTopByOrderByIdAsc();
        // If the item is already in the warehouse, update the quantity
        for (ItemNumberPairEntity line : warehouse.getLines()) {
            if (line.getItem().getId().equals(itemId)) {
                line.setQuantity(quantity);
                warehouseRepository.save(warehouse);
                return true;
            }
        }

        ItemNumberPairEntity newLine = new ItemNumberPairEntity();
        newLine.setItem(itemEntity.get());
        newLine.setQuantity(quantity);
        itemNumberPairRepository.save(newLine);
        warehouse.getLines().add(newLine);
        warehouseRepository.save(warehouse);
        return true;
    }
}
