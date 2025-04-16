package com.bestinsurance.api.dto;

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
    private String email;
    private String telephoneNumber;
    private AddressView address;
}
