package com.example.demo.model.availability;

import com.example.demo.model.ItemEntity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class WarehouseLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ItemEntity item;
    private int quantity;
}