package com.bestinsurance.api.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.SubscriptionCreateRequest;
import com.bestinsurance.api.dto.SubscriptionResponse;
import com.bestinsurance.api.dto.SubscriptionUpdateRequest;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.mapper.SubscriptionCreateMapper;
import com.bestinsurance.api.mapper.SubscriptionResponseMapper;
import com.bestinsurance.api.mapper.SubscriptionUpdateMapper;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.service.CrudService;
import com.bestinsurance.api.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/subscriptions")
public class SubscriptionController extends AbstractCrudController<SubscriptionCreateRequest, SubscriptionUpdateRequest, SubscriptionResponse, Subscription, SubscriptionId> {

    private final SubscriptionService subscriptionService;
    private final SubscriptionCreateMapper subscriptionCreateMapper;
    private final SubscriptionResponseMapper subscriptionResponseMapper;
    private final SubscriptionUpdateMapper subscriptionUpdateMapper;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  SubscriptionCreateMapper subscriptionCreateMapper,
                                  SubscriptionResponseMapper subscriptionResponseMapper,
                                  SubscriptionUpdateMapper subscriptionUpdateMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionCreateMapper = subscriptionCreateMapper;
        this.subscriptionResponseMapper = subscriptionResponseMapper;
        this.subscriptionUpdateMapper = subscriptionUpdateMapper;
    }

    @Override
    protected CrudService<Subscription, SubscriptionId> getService() {
        return subscriptionService;
    }

    @Override
    protected DTOMapper<SubscriptionCreateRequest, Subscription> getCreateDtoMapper() {
        return subscriptionCreateMapper;
    }

    @Override
    protected DTOMapper<SubscriptionUpdateRequest, Subscription> getUpdateDtoMapper() {
        return subscriptionUpdateMapper;
    }

    @Override
    protected DTOMapper<Subscription, SubscriptionResponse> getSearchDtoMapper() {
        return subscriptionResponseMapper;
    }

    @Override
    protected DTOMapper<Map<String, String>, SubscriptionId> getIdMapper() {
        return idMap -> {
            String idCustomer = Optional.ofNullable(idMap.get("idCustomer")).orElseThrow();
            String idPolicy = Optional.ofNullable(idMap.get("idPolicy")).orElseThrow();
            if (idCustomer.isBlank() || !idCustomer.matches(UUID_PATTERN)) {
                throw new IllegalArgumentException("idCustomer: Invalid UUID format");
            }
            if (idPolicy.isBlank() || !idPolicy.matches(UUID_PATTERN)) {
                throw new IllegalArgumentException("idPolicy: Invalid UUID format");
            }
            return SubscriptionId.builder()
                    .policyId(UUID.fromString(idPolicy))
                    .customerId(UUID.fromString(idCustomer))
                    .build();
        };
    }

    @Override
    @GetMapping(value = "/{idCustomer}/{idPolicy}", produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "idCustomer", schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = "idPolicy", schema = @Schema(type = "string"), required = true)
    public SubscriptionResponse searchById(@PathVariable Map<String, String> idDTO) {
        SubscriptionId id = getIdMapper().map(idDTO);
        Subscription foundSubscription = subscriptionService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id: " + id.toString() + " does not exist!"));
        return subscriptionResponseMapper.map(foundSubscription);
    }

    @Override
    @PutMapping(value = "/{idCustomer}/{idPolicy}", consumes = MEDIA_TYPE_JSON, produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "idCustomer", schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = "idPolicy", schema = @Schema(type = "string"), required = true)
    public SubscriptionResponse update(@PathVariable Map<String, String> idDTO, SubscriptionUpdateRequest updateDTO) {
        return super.update(idDTO, updateDTO);
    }

    @Override
    @DeleteMapping(value = "/{idCustomer}/{idPolicy}")
    @Parameter(in = ParameterIn.PATH, name = "idCustomer", schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = "idPolicy", schema = @Schema(type = "string"), required = true)
    public void delete(@PathVariable Map<String, String> idDTO) {
        super.delete(idDTO);
    }
}
