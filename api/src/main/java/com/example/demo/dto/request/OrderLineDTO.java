package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderLineDTO {

    @NotNull
    private Long itemId;

    @NotNull
    @Min(1)
    private int quantity;
}
