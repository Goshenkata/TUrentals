package com.example.demo.dto.response;

import com.example.demo.dto.common.MessageResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderResultDTO {
    List<Long> itemsThatCannotBeOrdered;
    MessageResponseDTO result;

    public CreateOrderResultDTO(MessageResponseDTO result) {
        this.result = result;
        itemsThatCannotBeOrdered = new ArrayList<>();
    }

    public CreateOrderResultDTO(MessageResponseDTO itemsNotAvailable, List<Long> invalidItems) {
        this.result = itemsNotAvailable;
        this.itemsThatCannotBeOrdered = invalidItems;
    }
}