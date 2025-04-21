package com.bestinsurance.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.bestinsurance.api.dto.CustomerView;
import com.bestinsurance.api.dto.PolicyView;
import com.bestinsurance.api.dto.SubscriptionResponse;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionResponseMapper extends DTOMapper<Subscription, SubscriptionResponse> {

    @Override
    @Mapping(target = "customer", source = "customer", qualifiedByName = "mapCustomerView")
    @Mapping(target = "policy", source = "policy", qualifiedByName = "mapPolicyView")
    SubscriptionResponse map(Subscription subscription);

    @Named("mapCustomerView")
    CustomerView mapCustomerView(Customer customer);

    @Named("mapPolicyView")
    PolicyView mapPolicyView(Policy policy);
}
