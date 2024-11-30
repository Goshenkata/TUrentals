package com.example.demo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemAvailibilityChangeDTO {
    private Long itemId;
    private Integer quantity;
}
