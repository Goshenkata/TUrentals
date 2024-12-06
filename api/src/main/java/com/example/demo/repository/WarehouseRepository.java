package com.example.demo.repository;

import com.example.demo.model.ItemEntity;
import com.example.demo.model.availability.WarehouseLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseLineEntity, Long> {
    WarehouseLineEntity findByItem(ItemEntity item);
}
