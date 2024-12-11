package com.example.demo.dto.response;

import com.example.demo.dto.enums.SortBy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class ItemDTO {
    Long id;
    String name;
    BigDecimal pricePerDay;
    String imageUrl;
    String categoryName;

    public static int compare(ItemDTO o1, ItemDTO o2, SortBy sortBy) {
        if (sortBy == SortBy.NAME_ASC || sortBy == SortBy.NAME_DESC) {
            return sortBy == SortBy.NAME_ASC ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
        } else if (sortBy == SortBy.PRICE_ASC || sortBy == SortBy.PRICE_DESC) {
            return sortBy == SortBy.PRICE_ASC ? o1.getPricePerDay().compareTo(o2.getPricePerDay()) : o2.getPricePerDay().compareTo(o1.getPricePerDay());
        }
        return 0;
    }
}
