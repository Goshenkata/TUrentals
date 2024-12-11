package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemAvailibilityChangeDTO {
    @Positive @NotNull
    private Long itemId;
    @PositiveOrZero @NotNull
    private Integer newQuantity;
}
