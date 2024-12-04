package com.example.demo.model;

import com.example.demo.dto.enums.OrderStatus;
import com.example.demo.model.address.AddressEntity;
import com.example.demo.model.availability.OrderLineEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(nullable = false)
    BigDecimal totalPrice;
    @Column(nullable = false)
     LocalDate deliveryDate;
    @Column(nullable = false)
    LocalDate returnDate;
    @OneToOne
    AddressEntity deliveryAddress;
    @Enumerated
    OrderStatus status;

    @ManyToOne
    UserEntity customer;

    @OneToMany(fetch = FetchType.EAGER)
    List<OrderLineEntity> lines;
}