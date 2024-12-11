package com.example.demo.controller;

import com.example.demo.dto.request.CheckoutRequestDTO;
import com.example.demo.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
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

// these don't seem to work
    @PostMapping("/webhooks")
    public ResponseEntity<Void> handleStripeEvent(@RequestBody String payload,
                                                  @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("test hit");

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
                handleCheckoutSessionCompleted(event);
                break;
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed(event);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok().build();
    }


    private void handleCheckoutSessionCompleted(Event event) {
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

        // Process order completion (e.g., mark order as paid, send confirmation email)
        // Add your business logic here
    }

    private void handlePaymentIntentSucceeded(Event event) {
        // Parse the event to a PaymentIntent object
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            System.out.println("PaymentIntent data missing in the event.");
            return;
        }

        System.out.println("Payment succeeded for PaymentIntent ID: " + paymentIntent.getId());

        // Process the successful payment (e.g., update database, notify customer)
        // Add your business logic here
    }

    private void handlePaymentIntentFailed(Event event) {
        // Parse the event to a PaymentIntent object
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            System.out.println("PaymentIntent data missing in the event.");
            return;
        }

        System.out.println("Payment failed for PaymentIntent ID: " + paymentIntent.getId());

        // Handle the failed payment (e.g., notify customer, retry payment)
        // Add your business logic here
    }
}
