package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CoverageUpdateRequest;
import com.bestinsurance.api.model.Coverage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoverageUpdateMapper extends DTOMapper<CoverageUpdateRequest, Coverage>{

    @Override
    Coverage map(CoverageUpdateRequest coverageUpdateRequest);
}
