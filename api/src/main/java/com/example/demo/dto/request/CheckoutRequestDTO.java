package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Data
@AllArgsConstructor
public class CheckoutRequestDTO {

    @NotNull
    @URL
    private String successUrl;

    @NotNull
    @URL
    private String cancelUrl;

    @NotNull
    private Set<OrderLineDTO> orderItems;
}

