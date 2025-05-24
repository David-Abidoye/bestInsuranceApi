package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CoverageCreateRequest;
import com.bestinsurance.api.model.Coverage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoverageCreateMapper extends DTOMapper<CoverageCreateRequest, Coverage>{

    @Override
    Coverage map(CoverageCreateRequest coverageCreateRequest);
}
