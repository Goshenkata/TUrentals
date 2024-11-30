package com.example.demo.repository;

import com.example.demo.model.availability.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemNumberPairRepository extends JpaRepository<OrderLineEntity, Long> {
}
