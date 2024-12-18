package com.example.demo.model.availability;

import com.example.demo.model.ItemEntity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class WarehouseLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private ItemEntity item;
    private int quantity;
}