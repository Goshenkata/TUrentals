package com.example.demo.dto.request;

import com.example.demo.dto.enums.CategoryEnum;
import com.example.demo.validation.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemCreateDTO {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Size(min = 3, max = 3000)
    private String description;
    @NotNull
    private BigDecimal pricePerDay;
    //todo. cloud file upload?
    private String imageUrl;
    @NotNull
    @Min(0)
    private Integer initialQuantity;
    @NotNull
    @ValidEnum(enumClass = CategoryEnum.class)
    private String categories;
}
