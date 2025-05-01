package com.bestinsurance.api.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.State;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;

public class PersistenceEntitiesUtil {

    public static Customer instanceCustomer(String name, String surname, String email, LocalDate birthDate, Address address) {
        return Customer.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate)
                .email(email)
                .address(address)
                .build();
    }

    public static Address instanceAddress(String street, String postalCode, City city) {
        return Address.builder()
                .city(city)
                .state(State.builder()
                        .id(city.getState().getId())
                        .build())
                .country(Country.builder()
                        .id(city.getCountry().getId())
                        .build())
                .addressLine(street)
                .postalCode(postalCode)
                .build();
    }

    public static Country instanceCountry(String name, int population) {
        return Country.builder()
                .name(name)
                .population(population)
                .build();
    }

    public static City instanceCity(String name, int population, State state, Country country) {
        return City.builder()
                .name(name)
                .population(population)
                .state(state)
                .country(country)
                .build();
    }

    public static State instanceState(String name, int population, Country country) {
        return State.builder()
                .name(name)
                .population(population)
                .country(country)
                .build();
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price) {
        return Policy.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price, Coverage... coverages) {
        Policy policy = instancePolicy(name, description, price);
        if (coverages != null && coverages.length > 0) {
            policy.setCoverages(Arrays.stream(coverages).collect(Collectors.toSet()));
        }
        return policy;
    }

    public static Subscription instanceSubscription(Customer customer, Policy policy, BigDecimal paidPrice,
                                                    LocalDate startDate, LocalDate endDate) {
        return Subscription.builder()
                .customer(customer)
                .policy(policy)
                .paidPrice(paidPrice)
                .startDate(startDate)
                .endDate(endDate)
                .id(SubscriptionId.builder()
                        .customerId(customer.getId())
                        .policyId(policy.getId())
                        .build())
                .build();
    }

    public static Coverage instanceCoverage(String name, String description) {
        return Coverage.builder()
                .name(name)
                .description(description)
                .build();
    }
}
