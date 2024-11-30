package com.example.demo.dto.request;

import jakarta.validation.constraints.Future;
import java.time.LocalDate;
import java.util.List;

public class OrderCreateDTO {
    @Future
    LocalDate devileryDate;
    @Future
    LocalDate returnDate;
    AddressDTO address;
    List<ItemNumberPairDTO> items;
}