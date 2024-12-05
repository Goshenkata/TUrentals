package com.example.demo.service;

import com.example.demo.dto.request.OrderLineDTO;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.availability.OrderLineEntity;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class StripeService {
    private final ModelMapper mapper;

    public StripeService(@Value("${stripe.api.key}") String apiKey, ModelMapper mapper) {
        this.mapper = mapper;
        Stripe.apiKey = apiKey; // Initialize Stripe API key
    }

    public String createCheckoutSession(Set<OrderLineDTO> orderLines, String successUrl, String cancelUrl) throws Exception {
        // Start building the session params
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl);

        // Add line items dynamically based on the orderItems set
        for (OrderLineDTO orderLineDTO : orderLines) {
            OrderLineEntity orderLine = mapper.map(orderLineDTO, OrderLineEntity.class);
            ItemEntity item = orderLine.getItem();
            if (item == null) {
                throw new IllegalArgumentException("Item not found. orderItem id: " + orderLine.getId());
            }

            long priceInStotinki = Math.round(item.getPricePerDay().multiply(BigDecimal.valueOf(100)).longValueExact()); // Convert BGN to stotinki

            // Add the item to the session
            sessionBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long)orderLine.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("bgn")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getName())
                                                            .putMetadata("itemId", item.getId().toString())
                                                            .build()
                                            )
                                            .setUnitAmount(priceInStotinki)
                                            .build()
                            )
                            .build()
            );
        }

        // Build and create the session
        SessionCreateParams params = sessionBuilder.build();
        Session session = Session.create(params);
        return session.getUrl(); // Return the session URL for redirection
    }

}
