package com.example.demo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderLineDTO {
    private ItemDTO item;
    private int quantity;
}
