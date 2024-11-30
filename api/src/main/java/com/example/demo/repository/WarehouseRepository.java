package com.example.demo.repository;

import com.example.demo.model.availability.WarehouseAvailabilityEnitity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<WarehouseAvailabilityEnitity, Long> {
    WarehouseAvailabilityEnitity findTopByOrderByIdAsc();
}
