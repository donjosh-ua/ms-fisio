package com.ms_fisio.subscription.domain.dto;

import lombok.Data;

@Data
public class CreateSubscriptionRequest {
    private Long userId;
    private Long planTypeId;
}
