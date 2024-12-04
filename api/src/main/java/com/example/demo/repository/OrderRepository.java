package com.example.demo.repository;

import com.example.demo.dto.enums.OrderStatus;
import com.example.demo.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    //find all orders between two dates
    @Query("SELECT o FROM OrderEntity o WHERE o.deliveryDate >= :startDate AND o.deliveryDate <= :endDate")
    List<OrderEntity> findAllDeliveriesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM OrderEntity o WHERE o.returnDate >= :startDate AND o.returnDate <= :endDate")
    List<OrderEntity> findAllPickupsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT o FROM OrderEntity o WHERE o.deliveryDate = :date")
    List<OrderEntity> findAllDelieriesOnDate(@Param("date") LocalDate date);

    @Query("SELECT o FROM OrderEntity o WHERE o.returnDate = :date")
    List<OrderEntity> findAllPickupsOnDate(@Param("date") LocalDate date);

    List<OrderEntity> findAllByDeliveryDateOrReturnDateAfterAndStatus(LocalDate deliveryDate, LocalDate returnDate, OrderStatus status);

}