package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CoverageResponse;
import com.bestinsurance.api.model.Coverage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoverageResponseMapper extends DTOMapper<Coverage, CoverageResponse>{

    @Override
    CoverageResponse map(Coverage coverage);
}
