package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.UUID_PATTERN;

import java.math.BigDecimal;
import java.util.Set;
import com.bestinsurance.api.validation.annotation.NotEmptyBody;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@NotEmptyBody
public class PolicyUpdateRequest {

    private String name;
    private String description;
    @Digits(integer = 4, fraction = 2, message = "Invalid price entry")
    private BigDecimal price;
    @Size(min = 1, message = "Coverages cannot be empty, a policy must have at least one coverage!")
    private Set<@NotBlank @Pattern(regexp = UUID_PATTERN,
            message = "Invalid UUID format") String> coverages;
}
