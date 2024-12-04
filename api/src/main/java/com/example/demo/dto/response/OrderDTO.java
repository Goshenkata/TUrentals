package com.example.demo.dto.response;

import com.example.demo.dto.enums.OrderType;
import com.example.demo.dto.request.AddressDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    private String note;
    private AddressDTO deliveryAddress;
    private List<OrderLineDTO> lines;
    private OrderType orderType;
    private UserDto assignenedTo;

    public static int compare(OrderDTO o1, OrderDTO o2) {
        //if both are deliveries, sort by delivery date
        if (o1.getOrderType().equals(OrderType.DELIVERY) && o2.getOrderType().equals(OrderType.DELIVERY)) {
            return o1.getDeliveryDate().compareTo(o2.getDeliveryDate());
        }
        //if both are pickups, sort by return date
        if (o1.getOrderType().equals(OrderType.PICKUP) && o2.getOrderType().equals(OrderType.PICKUP)) {
            return o1.getReturnDate().compareTo(o2.getReturnDate());
        }
        //if the left is delivery and the other is pickup
        if (o1.getOrderType().equals(OrderType.DELIVERY) && o2.getOrderType().equals(OrderType.PICKUP)) {
            return o1.getDeliveryDate().compareTo(o2.getReturnDate());
        }
        //if the left is pickup and the other is delivery
        return o1.getReturnDate().compareTo(o2.getDeliveryDate());
    }


}