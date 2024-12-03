package com.example.demo.controller;

import com.example.demo.service.StripeService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${stripe.domain}")
    private String yourDomain;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(
            ) {
            //@RequestParam String successUrl{
        System.out.println("test 1");

        try {
            String successUrl = yourDomain + "/success.html";
            String cancelUrl = yourDomain + "/cancel.html";

            // Get the checkout session URL from the StripeService
            String sessionUrl = stripeService.createCheckoutSession(successUrl, cancelUrl);

            // Set up the redirect in the response headers
            System.out.println("test 2");
            return ResponseEntity.ok(sessionUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
