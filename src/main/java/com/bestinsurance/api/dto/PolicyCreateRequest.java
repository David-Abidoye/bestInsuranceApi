package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.UUID_PATTERN;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PolicyCreateRequest {

    @NotBlank(message = "Policy must have a name, cannot be blank")
    private String name;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Policy must have price")
    @Digits(integer = 4, fraction = 2, message = "Invalid price entry")
    private BigDecimal price;
    @NotEmpty(message = "Coverages cannot be empty, a policy must have at least one coverage!")
    private Set<@NotBlank @Pattern(regexp = UUID_PATTERN,
            message = "Invalid UUID format") String> coverages;
}
