package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersThatNeedAttentionDTO {
    OrderDTO order;
    List<OrderLineDTO> missingItemsQuantity;
}
