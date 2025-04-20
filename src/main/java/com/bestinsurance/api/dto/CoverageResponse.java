package com.bestinsurance.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoverageResponse {

    private String id;
    private String name;
    private String description;
}
