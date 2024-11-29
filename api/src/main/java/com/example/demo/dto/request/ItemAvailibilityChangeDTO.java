package com.example.demo.dto.request;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemAvailibilityChangeDTO {
    private Long itemId;
    private Integer quantity;
    @Future
    private LocalDate dateFrom;
}
