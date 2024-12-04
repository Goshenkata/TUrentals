package com.example.demo.dto.request;

import com.example.demo.dto.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignToEmployeeDTO {
    private Long orderId;
    private Long employeeId;
    private OrderType orderType;
}
