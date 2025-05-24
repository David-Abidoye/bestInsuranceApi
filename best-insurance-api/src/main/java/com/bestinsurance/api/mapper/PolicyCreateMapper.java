package com.bestinsurance.api.mapper;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.PolicyCreateRequest;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Policy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PolicyCreateMapper extends DTOMapper<PolicyCreateRequest, Policy> {

    @Override
    @Mapping(target = "coverages", source = "coverages", qualifiedByName = "mapCoverages")
    Policy map(PolicyCreateRequest policyCreateRequest);

    @Named("mapCoverages")
    default Set<Coverage> mapCoverages(Set<String> coverageIdSet) {
        return Optional.ofNullable(coverageIdSet)
                .map(coverageIds -> coverageIds.stream()
                        .map(coverageId -> Coverage.builder()
                                .id(UUID.fromString(coverageId))
                                .build())
                        .collect(Collectors.toSet()))
                .orElse(null);
    }
}
