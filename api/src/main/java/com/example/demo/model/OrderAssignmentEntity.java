package com.example.demo.model;

import com.example.demo.dto.enums.OrderType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class OrderAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private OrderEntity order;
    @ManyToOne
    private UserEntity employee;
    @Enumerated
    private OrderType orderType;
}
