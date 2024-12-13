package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/webhooks")
    public ResponseEntity<MessageResponseDTO> handleStripeEvent(@RequestBody String payload,
                                                                @RequestHeader("Stripe-Signature") String sigHeader) {
        MessageResponseDTO messageResponseDTO;

        Event event;
        try {
            // Validate the webhook signature
            event = Webhook.constructEvent(payload, sigHeader, "whsec_9cc35b86e9a840f8b9c21507fb527ad6bef85a19aec8b9c88dcd0b637b8d4881");
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
