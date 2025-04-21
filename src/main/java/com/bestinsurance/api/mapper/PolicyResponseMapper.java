package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.PolicyResponse;
import com.bestinsurance.api.model.Policy;

@Mapper(uses = CoverageResponseMapper.class, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PolicyResponseMapper extends DTOMapper<Policy, PolicyResponse> {

    @Override
    @Mapping(target = "coverages", source = "coverages", qualifiedByName = "mapCoverages")
    PolicyResponse map(Policy policy);
}
