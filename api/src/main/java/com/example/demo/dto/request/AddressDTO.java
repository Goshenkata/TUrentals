package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotNull @NotBlank
    private String countryName;
    @NotNull @NotBlank
    private String stateName;
    @NotNull @NotBlank
    private String townName;
    @NotNull @NotBlank
    private String street;
    private String postCodeCode;
    private String description;
}