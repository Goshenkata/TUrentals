package com.example.demo.service;

import com.example.demo.dto.request.OrderLineDTO;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.availability.OrderLineEntity;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
public class StripeService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public StripeService(@Value("${stripe.api.key}") String apiKey, ModelMapper mapper, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.mapper = mapper;
        Stripe.apiKey = apiKey; // Initialize Stripe API key
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public String createCheckoutSession(Set<OrderLineDTO> orderLines, String successUrl, String cancelUrl, Long orderId) throws Exception {
        // Start building the session params
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl);

        if (orderRepository.findById(orderId).isPresent())
            sessionBuilder.putMetadata("orderId", ("" + orderId));
        else
            throw new IllegalArgumentException("Order not found. order id: " + orderId);

        // Add line items dynamically based on the orderItems set
        for (OrderLineDTO orderLineDTO : orderLines) {
            OrderLineEntity orderLine = mapper.map(orderLineDTO, OrderLineEntity.class);
            ItemEntity item = orderLine.getItem();
            if (item == null || item.getId() == null) {
                throw new IllegalArgumentException("Item not found. orderItem id: " + orderLine.getId());
            }

            item = itemRepository.findById(item.getId()).orElseThrow(() -> new IllegalArgumentException("Item not found."));

            if (item.getPricePerDay() == null) {
                throw new NullPointerException("Item has no price per day. Item id: " + item.getId());
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

    public void handleCheckoutSessionCompleted(Event event) {
        // Parse the event to a Session object
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) {
            System.out.println("PaymentIntent data missing in the event.");
            return;
        }

        // Extract relevant details from the session
        String sessionId = session.getId();
        String customerEmail = session.getCustomerDetails().getEmail();

        System.out.println("Checkout Session completed for session ID: " + sessionId + " and customer email: " + customerEmail);

        Map<String, String> metadata = session.getMetadata();

        String orderIdStr = metadata.get("orderId");
        Long orderId;
        try {
            orderId = Long.parseLong(orderIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse orderId: " + orderIdStr);
            return;
        }

        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found for ID: " + orderId));

        if (orderEntity.getStatus() == OrderStatus.IN_PROGRESS) {
            System.out.println("Order already marked as in progress.");
            return;
        }

        orderEntity.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(orderEntity);

        System.out.println("Order ID: " + orderId + " status updated to: " + orderEntity.getStatus());
        // Process order completion (e.g., mark order as paid)
        // Add your business logic here
    }

}
