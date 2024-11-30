package com.example.demo.dto.request;

import com.example.demo.validation.DeliveryBeforeReturn;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DeliveryBeforeReturn // Custom validation
public class OrderCreateDTO {
    @Future
    private LocalDate deliveryDate;
    @Future
    private LocalDate returnDate;
    private AddressDTO address;
    private List<ItemNumberPairDTO> items;
}