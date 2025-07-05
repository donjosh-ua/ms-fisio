package com.ms_fisio.subscription.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.ms_fisio.shared.domain.enums.DurationUnit;
import com.ms_fisio.user.domain.model.UserModel;

/**
 * Subscription entity linking users to plan types
 */
@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "duration_value", nullable = false)
    private Integer durationValue;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "duration_unit", length = 20, nullable = false)
    private DurationUnit durationUnit;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_type_id", nullable = false)
    private PlanTypeModel planType;
}
