package com.bestinsurance.api.model.embedded;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubscriptionId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "POLICY_ID", nullable = false)
    private UUID policyId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private UUID customerId;
}
