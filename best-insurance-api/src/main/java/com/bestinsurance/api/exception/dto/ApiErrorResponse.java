package com.bestinsurance.api.exception.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ApiErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();

}
