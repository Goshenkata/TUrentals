package com.example.demo.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {

    public StripeService(String apiKey) {
        Stripe.apiKey = apiKey; // Initialize Stripe with API key
    }

    public String createCheckoutSession(String successUrl, String cancelUrl, String priceId) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice(priceId) // Use the exact Price ID from your Stripe account
                                .build()
                )
                .build();
        Session session = Session.create(params);
        return session.getUrl(); // Return the session URL for redirection
    }
}
