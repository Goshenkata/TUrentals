package com.example.demo.dto.request;

import com.example.demo.dto.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCompleteDTO {
    @NotNull @Positive
    private Long orderId;
    @NotNull
    private OrderStatus orderStatus;
    private String note;
}
