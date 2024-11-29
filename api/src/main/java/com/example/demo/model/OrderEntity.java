package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
     LocalDate deliveryData;
    @Column(nullable = false)
    LocalDate returnDate;
    @OneToOne
    AdressEntity deliveryAddress;

    @ElementCollection
    @MapKeyJoinColumn(name = "item_entity_id")
    @Column(name = "value")
    Map<ItemEntity, Integer> orderedItems;
}
