package com.bestinsurance.api.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.CoverageCreateRequest;
import com.bestinsurance.api.dto.CoverageResponse;
import com.bestinsurance.api.dto.CoverageUpdateRequest;
import com.bestinsurance.api.mapper.CoverageCreateMapper;
import com.bestinsurance.api.mapper.CoverageResponseMapper;
import com.bestinsurance.api.mapper.CoverageUpdateMapper;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.service.CoverageService;
import com.bestinsurance.api.service.CrudService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/coverages")
@SecurityRequirement(name = "bestInsurance")
public class CoverageController extends AbstractSimpleIdCrudController<CoverageCreateRequest, CoverageUpdateRequest, CoverageResponse, Coverage> {

    private final CoverageService coverageService;
    private final CoverageCreateMapper coverageCreateMapper;
    private final CoverageUpdateMapper coverageUpdateMapper;
    private final CoverageResponseMapper coverageResponseMapper;

    public CoverageController(CoverageService coverageService,
                              CoverageCreateMapper coverageCreateMapper,
                              CoverageUpdateMapper coverageUpdateMapper,
                              CoverageResponseMapper coverageResponseMapper) {
        this.coverageService = coverageService;
        this.coverageCreateMapper = coverageCreateMapper;
        this.coverageUpdateMapper = coverageUpdateMapper;
        this.coverageResponseMapper = coverageResponseMapper;
    }

    @Override
    protected CrudService<Coverage, UUID> getService() {
        return coverageService;
    }

    @Override
    protected DTOMapper<CoverageCreateRequest, Coverage> getCreateDtoMapper() {
        return coverageCreateMapper;
    }

    @Override
    protected DTOMapper<CoverageUpdateRequest, Coverage> getUpdateDtoMapper() {
        return coverageUpdateMapper;
    }

    @Override
    protected DTOMapper<Coverage, CoverageResponse> getSearchDtoMapper() {
        return coverageResponseMapper;
    }

    @Override
    @GetMapping(value = "/{id}", produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public CoverageResponse searchById(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        Coverage foundCoverage = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coverage with id: " + id + " does not exist!"));
        return getSearchDtoMapper().map(foundCoverage);
    }
}
