package com.example.demo.dto.response;

import com.example.demo.dto.common.MessageResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderResultDTO {
    List<OrderLineDTO> itemsThatCannotBeOrdered;
    MessageResponseDTO result;
    String stripeSessionUrl;

    public CreateOrderResultDTO(MessageResponseDTO result, String stripeSessionUrl) {
        this.result = result;
        itemsThatCannotBeOrdered = new ArrayList<>();
        this.stripeSessionUrl = stripeSessionUrl;
    }

    public CreateOrderResultDTO(MessageResponseDTO itemsNotAvailable, List<OrderLineDTO> invalidItems) {
        this.result = itemsNotAvailable;
        this.itemsThatCannotBeOrdered = invalidItems;
    }
}