package com.bestinsurance.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "SUBSCRIPTIONS")
public class Subcription {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private SubscriptionId id;

    @ManyToOne
    @MapsId("policyId")
    @JoinColumn(name = "POLICY_ID", nullable = false)
    private Policy policy;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @NotNull
    @Column(name="START_DATE", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name="END_DATE", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "PAID_PRICE", precision = 4, scale = 2, nullable = false)
    private BigDecimal paidPrice;

    @NotNull
    @Column(name="CREATED", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name="UPDATED", nullable = false)
    private OffsetDateTime updatedAt;
}
