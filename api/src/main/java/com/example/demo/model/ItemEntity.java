package com.example.demo.model;

import com.example.demo.dto.enums.CategoryEnum;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    private String name;
    @Column(length = 4000)
    private String description;
    @Column(nullable = false)
    private BigDecimal pricePerDay;
    @Column
    private String imageUrl;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer initialQuantity;
    @Enumerated
    private CategoryEnum categories;
}
