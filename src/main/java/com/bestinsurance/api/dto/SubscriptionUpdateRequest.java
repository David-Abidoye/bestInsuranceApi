package com.bestinsurance.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.bestinsurance.api.validation.annotation.NotEmptyBody;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@NotEmptyBody
public class SubscriptionUpdateRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Digits(integer = 6, fraction = 2)
    private BigDecimal paidPrice;
}
