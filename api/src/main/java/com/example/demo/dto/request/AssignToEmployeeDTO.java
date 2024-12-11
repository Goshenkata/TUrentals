package com.example.demo.dto.request;

import com.example.demo.dto.enums.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignToEmployeeDTO {
    @NotNull @Positive
    private Long orderId;
    @NotNull @Positive
    private Long employeeId;
    @NotNull
    private OrderType orderType;
}
