package com.bestinsurance.api.controller;

import static com.bestinsurance.api.validation.utils.ValidationUtils.parsePolicyOrderByFilter;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parsePriceFilter;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parseStringFilter;

import java.math.BigDecimal;
import java.util.List;
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
import com.bestinsurance.api.service.PolicyService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/policies")
@SecurityRequirement(name = "bestInsurance")
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
    protected PolicyService getService() {
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

    @Override
    @Parameter(in = ParameterIn.QUERY, name = "nameContains", description = "filters all the policies with a name containing a string")
    @Parameter(in = ParameterIn.QUERY, name = "price", description = "filters all the policies with a price equal to a number", example = "100")
    @Parameter(in = ParameterIn.QUERY, name = "priceMoreThan", description = "filters all the policies with a price greater than a number", example = "100")
    @Parameter(in = ParameterIn.QUERY, name = "priceLessThan", description = "filters all the policies with a price less than a number", example = "100")
    @Parameter(in = ParameterIn.QUERY, name = "orderBy", description = "a string parameter that indicates which field (name or price) should be used for sorting", schema = @Schema(type = "string", allowableValues = {"NAME", "PRICE"}))
    public List<PolicyResponse> all(Map<String, String> filters) {

        BigDecimal priceMoreThan = parsePriceFilter(filters, "priceMoreThan");
        BigDecimal priceLessThan = parsePriceFilter(filters, "priceLessThan");
        BigDecimal price = parsePriceFilter(filters, "price");
        String nameContains = parseStringFilter(filters, "nameContains");
        PolicyService.PolicyOrderBy orderBy = parsePolicyOrderByFilter(filters.get("orderBy"));

        List<Policy> allDomainObjects = getService().findAllWithFilters(priceMoreThan, priceLessThan, price, nameContains, orderBy);
        return allDomainObjects.stream()
                .map(getSearchDtoMapper()::map)
                .toList();
    }
}
