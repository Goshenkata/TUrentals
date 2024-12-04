package com.example.demo.dto.response;

import com.example.demo.dto.enums.OrderType;
import com.example.demo.dto.request.AddressDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private BigDecimal totalPrice;
    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private AddressDTO deliveryAddress;
    private List<OrderLineDTO> lines;
    private OrderType orderType;
    private UserDto assignenedTo;
}