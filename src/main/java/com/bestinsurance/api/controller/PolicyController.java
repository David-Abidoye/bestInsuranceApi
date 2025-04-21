package com.bestinsurance.api.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.PolicyCreateRequest;
import com.bestinsurance.api.dto.PolicyResponse;
import com.bestinsurance.api.dto.PolicyUpdateRequest;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.mapper.PolicyCreateMapper;
import com.bestinsurance.api.mapper.PolicyResponseMapper;
import com.bestinsurance.api.mapper.PolicyUpdateMapper;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.service.CrudService;
import com.bestinsurance.api.service.PolicyService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/policies")
public class PolicyController extends AbstractSimpleIdCrudController<PolicyCreateRequest, PolicyUpdateRequest, PolicyResponse, Policy> {

    private final PolicyService policyService;
    private final PolicyCreateMapper policyCreateMapper;
    private final PolicyUpdateMapper policyUpdateMapper;
    private final PolicyResponseMapper policyResponseMapper;

    public PolicyController(PolicyService policyService,
                            PolicyCreateMapper policyCreateMapper,
                            PolicyUpdateMapper policyUpdateMapper,
                            PolicyResponseMapper policyResponseMapper) {
        this.policyService = policyService;
        this.policyCreateMapper = policyCreateMapper;
        this.policyUpdateMapper = policyUpdateMapper;
        this.policyResponseMapper = policyResponseMapper;
    }

    @Override
    protected CrudService<Policy, UUID> getService() {
        return policyService;
    }

    @Override
    protected DTOMapper<PolicyCreateRequest, Policy> getCreateDtoMapper() {
        return policyCreateMapper;
    }

    @Override
    protected DTOMapper<PolicyUpdateRequest, Policy> getUpdateDtoMapper() {
        return policyUpdateMapper;
    }

    @Override
    protected DTOMapper<Policy, PolicyResponse> getSearchDtoMapper() {
        return policyResponseMapper;
    }

    @Override
    @GetMapping(value = "/{id}", produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public PolicyResponse searchById(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        Policy foundPolicy = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Policy with id: " + id + " does not exist!"));
        return getSearchDtoMapper().map(foundPolicy);
    }
}
