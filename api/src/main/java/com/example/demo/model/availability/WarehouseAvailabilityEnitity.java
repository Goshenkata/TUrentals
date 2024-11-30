package com.example.demo.model.availability;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class WarehouseAvailabilityEnitity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ItemNumberPairEntity> lines;
}
