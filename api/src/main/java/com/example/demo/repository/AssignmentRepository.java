package com.example.demo.repository;

import com.example.demo.dto.enums.OrderType;
import com.example.demo.model.OrderAssignmentEntity;
import com.example.demo.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<OrderAssignmentEntity, Long> {
    Optional<OrderAssignmentEntity> findByOrderAndOrderType(OrderEntity order, OrderType orderType);

}
