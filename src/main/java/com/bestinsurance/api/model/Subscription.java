package com.bestinsurance.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
@EntityListeners(AuditingEntityListener.class)
public class Subscription implements DomainObject<SubscriptionId> {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private SubscriptionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("policyId")
    @JoinColumn(name = "POLICY_ID", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.EAGER)
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
    @Column(name = "PAID_PRICE", precision = 6, scale = 2, nullable = false)
    private BigDecimal paidPrice;

    @CreatedDate
    @Column(name="CREATED", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name="UPDATED", nullable = false)
    private OffsetDateTime updatedAt;
}
