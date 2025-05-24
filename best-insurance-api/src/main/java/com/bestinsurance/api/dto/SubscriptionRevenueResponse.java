package com.bestinsurance.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRevenueResponse {
    private String stateName;
    private BigDecimal revenue;
    private Long customersCount;
}
