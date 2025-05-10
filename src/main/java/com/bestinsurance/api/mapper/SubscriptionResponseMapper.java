package com.bestinsurance.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.bestinsurance.api.dto.CustomerView;
import com.bestinsurance.api.dto.PolicyView;
import com.bestinsurance.api.dto.SubscriptionLogMsg;
import com.bestinsurance.api.dto.SubscriptionResponse;
import com.bestinsurance.api.dto.SubscriptionRevenueResponse;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.SubscriptionRevenue;

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

    SubscriptionRevenueResponse mapSubscriptionRevenueResponse(SubscriptionRevenue subscriptionRevenue);

    default List<SubscriptionRevenueResponse> mapSubscriptionRevenueResponses(List<SubscriptionRevenue> subscriptionRevenues) {
        return subscriptionRevenues.stream()
                .map(this::mapSubscriptionRevenueResponse)
                .toList();
    }

    @Mapping(target = "policyId", source = "id.policyId")
    @Mapping(target = "customerId", source = "id.customerId")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerSurname", source = "customer.surname")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "customerTelephone", source = "customer.telephoneNumber")
    @Mapping(target = "policyName", source = "policy.name")
    @Mapping(target = "subscriptionStart", source = "startDate")
    @Mapping(target = "subscriptionEnd", source = "endDate")
    SubscriptionLogMsg mapSubscriptionToMsg(Subscription subscription);
}
