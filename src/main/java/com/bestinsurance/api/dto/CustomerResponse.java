package com.bestinsurance.api.dto;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_PATTERN;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CustomerResponse {
    private String id;
    private String name;
    private String surname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate birthDate;
    private String email;
    private String telephoneNumber;
    private AddressView address;
}
