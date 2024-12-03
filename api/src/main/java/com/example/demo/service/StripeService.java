package com.example.demo.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.api.key}")String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public String createCheckoutSession(String successUrl, String cancelUrl) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(5L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("bgn")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("asddadasd")
                                                        .putMetadata("itemIdKey","ItemIdValue")
                                                        .build()
                                            )
                                            .setUnitAmount(5L) // Required
                                            .build()
                                )
                                //.setPrice(priceId)
                                .build()
                )
                .putMetadata("orderIdKey", "1")
                .build();
        Session session = Session.create(params);
        return session.getUrl(); // Return the session URL for redirection
    }
}
