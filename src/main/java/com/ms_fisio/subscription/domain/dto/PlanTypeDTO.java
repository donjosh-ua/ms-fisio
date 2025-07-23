package com.ms_fisio.subscription.domain.dto;

import lombok.Data;

@Data
public class PlanTypeDTO {
    private Long planTypeId;
    private String name;
    private Double price;
}