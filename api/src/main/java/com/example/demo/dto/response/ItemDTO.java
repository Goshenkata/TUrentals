package com.example.demo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class ItemDTO {
    Long id;
    String name;
    BigDecimal pricePerDay;
    String imageUrl;
    String category;
}
