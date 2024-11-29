package com.example.demo.repository;

import com.example.demo.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // Method 1: Orders with delivery date before a specific date containing a specific item
    @Query("SELECT o FROM OrderEntity o JOIN o.orderedItems oi " +
            "WHERE o.deliveryData <= :date AND KEY(oi).id = :itemId")
    List<OrderEntity> findOrdersByDeliveryDateBeforeAndItemId(@Param("date") LocalDate date,
                                                              @Param("itemId") Long itemId);

    // Method 2: Orders between two dates containing a specific item
    @Query("SELECT o FROM OrderEntity o JOIN o.orderedItems oi " +
            "WHERE o.returnDate BETWEEN :startDate AND :endDate AND KEY(oi).id = :itemId")
    List<OrderEntity> findOrdersByReturnDateBetweenAndItemId(@Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate endDate,
                                                             @Param("itemId") Long itemId);
}
