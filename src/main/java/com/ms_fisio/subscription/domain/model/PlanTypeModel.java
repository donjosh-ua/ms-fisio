package com.ms_fisio.subscription.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Plan type entity representing subscription plans
 */
@Entity
@Table(name = "plan_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanTypeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_type_id")
    private Long planTypeId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    // Relationships
    @OneToMany(mappedBy = "planType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionModel> subscriptions;
}
