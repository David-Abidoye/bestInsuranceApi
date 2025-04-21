package com.bestinsurance.api.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.CoverageResponse;
import com.bestinsurance.api.model.Coverage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoverageResponseMapper extends DTOMapper<Coverage, CoverageResponse>{

    @Override
    CoverageResponse map(Coverage coverage);

    @Named("mapCoverages")
    default Set<CoverageResponse> mapCoverages(Set<Coverage> coverages){
        return coverages.stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }
}
