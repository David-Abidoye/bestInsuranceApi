package com.bestinsurance.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerCreateRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Invalid telephone number format")
    private String telephoneNumber;

    @NotBlank(message = "Street cannot be blank")
    @Pattern(regexp = "^\\S[\\S ]*\\S$")
    @JsonProperty("address")
    private String street;

    @Digits(integer = 6, fraction = 0, message = "Postal code should be digits")
    @Size(min = 6, max = 6, message = "Postal code should have 6 digits")
    private String postalCode;

    @NotBlank(message = "Country id cannot be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idCountry;

    @NotBlank(message = "City id cannot be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idCity;

    @NotBlank(message = "State id cannot be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String idState;
}
