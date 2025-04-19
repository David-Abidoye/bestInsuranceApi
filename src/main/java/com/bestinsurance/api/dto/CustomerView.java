package com.bestinsurance.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerView {

    private String id;
    private String name;
    private String surname;
    private String email;
}
