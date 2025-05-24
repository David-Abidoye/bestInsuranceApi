package com.bestinsurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AddressView {

    private String id;
    private String address;
    private String postalCode;
    private CountryView country;
    private CityView city;
    private StateView state;
}
