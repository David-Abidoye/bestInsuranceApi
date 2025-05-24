package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.PolicyUpdateRequest;
import com.bestinsurance.api.model.Policy;

@Mapper(uses = PolicyCreateMapper.class, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PolicyUpdateMapper extends DTOMapper<PolicyUpdateRequest, Policy> {
    @Override
    @Mapping(target = "coverages", source = "coverages", qualifiedByName = "mapCoverages")
    Policy map(PolicyUpdateRequest policyUpdateRequest);
}
