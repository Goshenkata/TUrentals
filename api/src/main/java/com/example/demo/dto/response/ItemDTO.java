package com.example.demo.dto.response;

import com.example.demo.dto.enums.SortBy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Data
public class ItemDTO {
    Long id;
    String name;
    BigDecimal pricePerDay;
    String imageUrl;
    String categoryName;
    String description;

    public static int compare(ItemDTO o1, ItemDTO o2, SortBy sortBy) {
        if (sortBy == SortBy.NAME_ASC || sortBy == SortBy.NAME_DESC) {
            return sortBy == SortBy.NAME_ASC ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
        } else if (sortBy == SortBy.PRICE_ASC || sortBy == SortBy.PRICE_DESC) {
            return sortBy == SortBy.PRICE_ASC ? o1.getPricePerDay().compareTo(o2.getPricePerDay()) : o2.getPricePerDay().compareTo(o1.getPricePerDay());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
