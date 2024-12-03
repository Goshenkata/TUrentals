package com.example.demo.controller;

import com.example.demo.service.StripeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
public class StripeController {

    private final StripeService stripeService;

    public StripeController() {
        // Initialize with your Stripe secret key
        // todo check my stripe secret key
        this.stripeService = new StripeService("sk_test_z6Wgj3W5n3eYSLEKPRJ4OrE900vpjOnFhP");
    }

    @GetMapping("/create-checkout-session")
    public ResponseEntity<Void> createCheckoutSession(
            @RequestParam String priceId) {
        try {
            String YOUR_DOMAIN = "http://localhost:4242";
            String successUrl = YOUR_DOMAIN + "/success.html";
            String cancelUrl = YOUR_DOMAIN + "/cancel.html";

            // Get the checkout session URL from the StripeService
            String sessionUrl = stripeService.createCheckoutSession(successUrl, cancelUrl, priceId);

            // Set up the redirect in the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(sessionUrl));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER); // 303 redirect
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
