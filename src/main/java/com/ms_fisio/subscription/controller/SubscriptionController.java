package com.ms_fisio.subscription.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms_fisio.subscription.domain.dto.CreateSubscriptionRequest;
import com.ms_fisio.subscription.service.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateSubscriptionRequest request) {
        log.info(">>>> Entró al endpoint POST /create");
        log.info("Recibida solicitud de creación: {}", request);
        return subscriptionService.createOrder(request);
    }

    @GetMapping("/capture")
    public ResponseEntity<String> capture(@RequestParam("token") String token) {
        return subscriptionService.captureOrder(token);
    }
}
