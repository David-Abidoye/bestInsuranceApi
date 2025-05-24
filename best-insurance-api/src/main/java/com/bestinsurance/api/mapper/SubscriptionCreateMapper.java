package com.bestinsurance.api.mapper;

import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.SubscriptionCreateRequest;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionCreateMapper extends DTOMapper<SubscriptionCreateRequest, Subscription> {

    @Override
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer")
    @Mapping(target = "policy", source = "policyId", qualifiedByName = "mapPolicy")
    Subscription map(SubscriptionCreateRequest subscriptionCreateRequest);

    @Named("mapCustomer")
    default Customer mapCustomer(String customerId) {
        return Customer.builder().id(UUID.fromString(customerId)).build();
    }

    @Named("mapPolicy")
    default Policy mapSubscription(String policyId) {
        return Policy.builder().id(UUID.fromString(policyId)).build();
    }

    @AfterMapping
    default void buildSubscriptionId(@MappingTarget Subscription subscription) {
        subscription.setId(SubscriptionId.builder().build());
    }
}
