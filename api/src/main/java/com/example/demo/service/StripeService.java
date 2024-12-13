package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.ItemNumberPairDTO;
import com.example.demo.dto.request.OrderLineDTO;
import com.example.demo.dto.response.CreateOrderResultDTO;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.OrderEntity;
import com.example.demo.dto.enums.OrderStatus;
import com.example.demo.model.availability.OrderLineEntity;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderLineRepository;
import com.example.demo.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.LineItemCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class StripeService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    public StripeService(@Value("${stripe.api.key}") String apiKey, ModelMapper mapper, ItemRepository itemRepository, OrderRepository orderRepository, OrderLineRepository orderLineRepository) {
        this.mapper = mapper;
        Stripe.apiKey = apiKey; // Initialize Stripe API key
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
    }

    public String createCheckoutSession(String orderId, List<ItemNumberPairDTO> orderLines, String successUrl, String cancelUrl) throws Exception {
        // Start building the session params
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putMetadata("orderId", orderId)
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                            .putMetadata("orderId", orderId)
                            .build()
                );

        for (ItemNumberPairDTO orderLine : orderLines) {
            ItemEntity item = itemRepository.findById(orderLine.getItemId()).orElseThrow(() -> new IllegalArgumentException("Item not found."));

            if (item.getPricePerDay() == null) {
                throw new NullPointerException("Item has no price per day. Item id: " + item.getId());
            }

            long priceInStotinki = Math.round(item.getPricePerDay().multiply(BigDecimal.valueOf(100)).longValueExact()); // Convert BGN leva to stotinki

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

    public MessageResponseDTO handleCheckoutSessionCompleted(Event event) {
        // Parse the event to a Session object
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) {
            log.error("Unable to process event: session data was null.");
            return new MessageResponseDTO(404, "Unable to process event: session data was null.");
        }

        String orderIdStr = session.getMetadata().get("orderId");
        Long orderId;
        try{
            orderId = Long.parseLong(orderIdStr);
        } catch (NumberFormatException e) {
            log.error("Failed to parse orderId: " + orderIdStr);
            return new MessageResponseDTO(409, "Failed to parse orderId: " + orderIdStr);
        }

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
        if (optionalOrderEntity.isEmpty()) {
            log.error("Order not found. Order ID: " + orderId);
            return new MessageResponseDTO(404, "Order not found. Order ID: " + orderId);
        }

        OrderEntity orderEntity = optionalOrderEntity.get();
        orderEntity.setStatus(OrderStatus.PENDING);
        orderRepository.save(orderEntity);

        log.info("Successfully changed order status to PENDING.");
        return new MessageResponseDTO(200, "Successfully changed order status to PENDING.");
    }

    public MessageResponseDTO handlePaymentIntentFailed(Event event) {
        // Parse the event to a PaymentIntent object
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            log.error("PaymentIntent data missing in the event.");
            return new MessageResponseDTO(404, "PaymentIntent data missing in the event.");
        }

        String orderIdStr = paymentIntent.getMetadata().get("orderId");
        Long orderId;
        try{
            orderId = Long.parseLong(orderIdStr);
        } catch (NumberFormatException e) {
            log.error("Failed to parse orderId: " + orderIdStr);
            return new MessageResponseDTO(409, "Failed to parse orderId: " + orderIdStr);
        }

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
        if (optionalOrderEntity.isEmpty()) {
            log.error("Order not found. Order ID: " + orderId);
            return new MessageResponseDTO(404, "Order not found. Order ID: " + orderId);
        }

        OrderEntity orderEntity = optionalOrderEntity.get();
        orderEntity.setStatus(OrderStatus.FAILEDPAYMENT);
        orderRepository.save(orderEntity);

        log.info("Successfully changed order status to FAILEDPAYMENT.");
        return new MessageResponseDTO(200, "Successfully changed order status to FAILEDPAYMENT.");
    }
}
