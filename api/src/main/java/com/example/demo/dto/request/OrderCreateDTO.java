package com.example.demo.dto.request;

import com.example.demo.validation.DeliveryBeforeReturn;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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
    @NotNull
    private AddressDTO address;
    private List<ItemNumberPairDTO> items;
    @NotNull
    @URL
    private String successUrl;
    @NotNull
    @URL
    private String cancelUrl;
}