package com.example.demo.repository;

import com.example.demo.model.availability.WarehouseLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<WarehouseLineEntity, Long> {
}
