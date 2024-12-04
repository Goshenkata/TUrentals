package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String countryName;
    private String stateName;
    private String townName;
    private String street;
    private String postCodeCode;
    private String description;
}