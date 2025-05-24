package com.bestinsurance.api.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRevenue {
    private String stateName;
    private BigDecimal revenue;
    private Long customersCount;
}
