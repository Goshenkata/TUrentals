package com.example.demo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderLineDTO {
    private Long itemId;
    private int quantity;
}
