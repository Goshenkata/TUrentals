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
            case "payment_intent.succeeded":
                messageResponseDTO = handlePaymentIntentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                messageResponseDTO = handlePaymentIntentFailed(event);
                break;
            default:
                return ResponseEntity.status(404).body(new MessageResponseDTO(404, "Unexpected event"));
        }

        return ResponseEntity.status(messageResponseDTO.status()).body(messageResponseDTO);
    }

    // todo implement
    private MessageResponseDTO handlePaymentIntentSucceeded(Event event) {
        // Parse the event to a PaymentIntent object
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            System.out.println("PaymentIntent data missing in the event.");
            return null;
        }

        System.out.println("Payment succeeded for PaymentIntent ID: " + paymentIntent.getId());
        return null;
        // Process the successful payment (e.g., update database, notify customer)
        // Add your business logic here
    }

    // todo implement
    private MessageResponseDTO handlePaymentIntentFailed(Event event) {
        // Parse the event to a PaymentIntent object
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            System.out.println("PaymentIntent data missing in the event.");
            return null;
        }

        System.out.println("Payment failed for PaymentIntent ID: " + paymentIntent.getId());
        return null;
        // Handle the failed payment (e.g., notify customer, retry payment)
        // Add your business logic here
    }
}
