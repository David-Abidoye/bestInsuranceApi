package com.bestinsurance.api.dto;

import com.bestinsurance.api.validation.annotation.NotEmptyBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@NotEmptyBody
public class CustomerUpdateRequest {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\S[\\S ]*\\S$")
    @JsonProperty("address")
    private String street;

    @Digits(integer = 6, fraction = 0, message = "Postal code should be digits")
    @Size(min = 6, max = 6, message = "Postal code should have 6 digits")
    private String postalCode;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idCountry;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idCity;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idState;
}
