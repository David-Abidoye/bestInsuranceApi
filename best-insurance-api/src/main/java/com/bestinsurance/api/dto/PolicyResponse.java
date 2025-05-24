package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_TIME_PATTERN;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PolicyResponse {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private OffsetDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private OffsetDateTime updatedAt;
    private Set<CoverageResponse> coverages;
}
