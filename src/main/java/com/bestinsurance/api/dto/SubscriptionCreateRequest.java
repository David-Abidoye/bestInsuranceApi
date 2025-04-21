package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_PATTERN;
import static com.bestinsurance.api.helper.ConstraintHelper.UUID_PATTERN;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionCreateRequest {

    @NotBlank(message = "Customer id cannot be blank")
    @Pattern(regexp = UUID_PATTERN, message = "Invalid UUID format")
    private String customerId;
    @NotBlank(message = "Policy id cannot be blank")
    @Pattern(regexp = UUID_PATTERN, message = "Invalid UUID format")
    private String policyId;
    @NotNull
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate startDate;
    @NotNull
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate endDate;
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private BigDecimal paidPrice;
}
