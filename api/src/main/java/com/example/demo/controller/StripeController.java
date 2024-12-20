package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Value("${stripe.webhook.secret}")
    private String secret;

    @PostMapping("/webhooks")
    public ResponseEntity<MessageResponseDTO> handleStripeEvent(@RequestBody String payload,
                                                                @RequestHeader("Stripe-Signature") String sigHeader, Principal principal) {
        MessageResponseDTO messageResponseDTO;

        Event event;
        try {
            // Validate the webhook signature
            event = Webhook.constructEvent(payload, sigHeader, secret);
        } catch (Exception e) {
            System.out.println("Webhook signature validation failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Process the event
        switch (event.getType()) {
            case "checkout.session.completed":
                messageResponseDTO = stripeService.handleCheckoutSessionCompleted(event);
                break;
            case "payment_intent.payment_failed":
                messageResponseDTO = stripeService.handlePaymentIntentFailed(event);
                break;
            default:
                return ResponseEntity.status(200).body(new MessageResponseDTO(200, "Unexpected event"));
        }

        return ResponseEntity.status(messageResponseDTO.status()).body(messageResponseDTO);
    }
}
