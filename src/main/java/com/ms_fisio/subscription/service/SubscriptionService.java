package com.ms_fisio.subscription.service;

import org.springframework.http.ResponseEntity;

import com.ms_fisio.subscription.domain.dto.CreateSubscriptionRequest;

public interface SubscriptionService {
    ResponseEntity<String> createOrder(CreateSubscriptionRequest request);

    ResponseEntity<String> captureOrder(String orderId);
}
