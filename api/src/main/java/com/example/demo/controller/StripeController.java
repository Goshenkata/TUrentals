package com.example.demo.controller;

import com.example.demo.dto.request.CheckoutRequestDTO;
import com.example.demo.service.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;

    @Value("${stripe.domain}")
    private String yourDomain;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(
            @RequestBody CheckoutRequestDTO checkoutRequest
    ) {
        try {
            // Validate request data
            if (checkoutRequest.getOrderItems() == null || checkoutRequest.getOrderItems().isEmpty()) {
                return ResponseEntity.badRequest().body("Order items cannot be empty.");
            }
            if (checkoutRequest.getSuccessUrl() == null || checkoutRequest.getCancelUrl() == null) {
                return ResponseEntity.badRequest().body("Success URL and Cancel URL are required.");
            }

            // Generate the Stripe checkout session URL
            String sessionUrl = stripeService.createCheckoutSession(
                    checkoutRequest.getOrderItems(),
                    checkoutRequest.getSuccessUrl(),
                    checkoutRequest.getCancelUrl()
            );

            return ResponseEntity.ok(sessionUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating checkout session.");
        }
    }
}
