package com.example.demo.dto.common;

import com.example.demo.dto.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {
    String nameQuery;
    BigDecimal priceFrom;
    BigDecimal priceTo;
    String category;
    SortBy sortBy;

}
