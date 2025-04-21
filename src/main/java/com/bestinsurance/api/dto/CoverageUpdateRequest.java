package com.bestinsurance.api.dto;

import com.bestinsurance.api.validation.annotation.NotEmptyBody;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@NotEmptyBody
public class CoverageUpdateRequest {

    private String name;
    private String description;
}
