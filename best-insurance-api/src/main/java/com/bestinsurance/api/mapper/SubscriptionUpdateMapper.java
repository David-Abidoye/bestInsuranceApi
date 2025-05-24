package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.bestinsurance.api.dto.SubscriptionUpdateRequest;
import com.bestinsurance.api.model.Subscription;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionUpdateMapper extends DTOMapper<SubscriptionUpdateRequest, Subscription> {

    @Override
    Subscription map(SubscriptionUpdateRequest subscriptionUpdateRequest);
}
