package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ItemCreateDTO {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Size(min = 3, max = 3000)
    private String description;
    @NotNull
    private BigDecimal pricePerDay;
    @URL
    private String imageUrl;
    @NotNull
    @Min(0)
    private Integer initialQuantity;
    @NotNull
    private Long categoryId;
}
