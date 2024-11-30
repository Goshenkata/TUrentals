package com.example.demo.validation;

import com.example.demo.dto.request.OrderCreateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeliveryBeforeReturnValidator implements ConstraintValidator<DeliveryBeforeReturn, OrderCreateDTO> {

    @Override
    public boolean isValid(OrderCreateDTO order, ConstraintValidatorContext context) {
        if (order.getDeliveryDate() == null || order.getReturnDate() == null) {
            return true; // Let @Future handle null checks
        }
        return order.getDeliveryDate().isBefore(order.getReturnDate());
    }
}

