package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_PATTERN;

import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionLogMsg {

    private UUID policyId;
    private UUID customerId;
    private String customerName;
    private String customerSurname;
    private String customerEmail;
    private String customerTelephone;
    private String policyName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate subscriptionStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate subscriptionEnd;
}
