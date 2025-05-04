package com.bestinsurance.api.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCsv {

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "surname")
    private String surname;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "birthDate")
    private String birthDate;

    @CsvBindByName(column = "address")
    private String address;

    @CsvBindByName(column = "postalCode")
    private String postalCode;

    @CsvBindByName(column = "stateName")
    private String stateName;

    @CsvBindByName(column = "cityName")
    private String cityName;

    @CsvBindByName(column = "policyName")
    private String policyName;

    @CsvBindByName(column = "paidPrice")
    private String paidPrice;

    @CsvBindByName(column = "startDate")
    private String startDate;

    @CsvBindByName(column = "endDate")
    private String endDate;
}
