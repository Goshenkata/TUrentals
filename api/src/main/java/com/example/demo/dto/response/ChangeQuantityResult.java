package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeQuantityResult {
    private Long itemId;
    private Integer newQuantity;
    private boolean success;
    private List<OrderDTO> affectedOrders;
}
