package com.bestinsurance.api.controller;

import static com.bestinsurance.api.helper.ConstraintHelper.UUID_PATTERN;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bestinsurance.api.dto.SubscriptionCreateRequest;
import com.bestinsurance.api.dto.SubscriptionResponse;
import com.bestinsurance.api.dto.SubscriptionRevenueResponse;
import com.bestinsurance.api.dto.SubscriptionUpdateRequest;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.mapper.SubscriptionCreateMapper;
import com.bestinsurance.api.mapper.SubscriptionResponseMapper;
import com.bestinsurance.api.mapper.SubscriptionUpdateMapper;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.service.CsvService;
import com.bestinsurance.api.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/subscriptions")
@SecurityRequirement(name = "bestInsurance")
public class SubscriptionController extends AbstractCrudController<SubscriptionCreateRequest, SubscriptionUpdateRequest, SubscriptionResponse, Subscription, SubscriptionId> {

    private final SubscriptionService subscriptionService;
    private final SubscriptionCreateMapper subscriptionCreateMapper;
    private final SubscriptionResponseMapper subscriptionResponseMapper;
    private final SubscriptionUpdateMapper subscriptionUpdateMapper;
    private final CsvService csvService;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  SubscriptionCreateMapper subscriptionCreateMapper,
                                  SubscriptionResponseMapper subscriptionResponseMapper,
                                  SubscriptionUpdateMapper subscriptionUpdateMapper, CsvService csvService) {
        this.subscriptionService = subscriptionService;
        this.subscriptionCreateMapper = subscriptionCreateMapper;
        this.subscriptionResponseMapper = subscriptionResponseMapper;
        this.subscriptionUpdateMapper = subscriptionUpdateMapper;
        this.csvService = csvService;
    }

    @Override
    protected SubscriptionService getService() {
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
    protected SubscriptionResponseMapper getSearchDtoMapper() {
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
        Subscription foundSubscription = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription with id: " + id.toString() + " does not exist!"));
        return getSearchDtoMapper().map(foundSubscription);
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

    @GetMapping("/revenues")
    public List<SubscriptionRevenueResponse> searchStateSubscriptionRevenue() {
        return getSearchDtoMapper().mapSubscriptionRevenueResponses(getService().findStateSubscriptionRevenue());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return csvService.upload(file);
    }
}
