package com.bestinsurance.api.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.bestinsurance.api.common.AbstractIntegrationTest;
import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.State;
import com.bestinsurance.api.repos.AddressRepository;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CountryRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.StateRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DomainConfig.class)
class CustomerJpaTest extends AbstractIntegrationTest {

    @Autowired
    private CustomerRepository testee;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private CityRepository cityRepo;
    @Autowired
    private StateRepository stateRepo;
    @Autowired
    private CountryRepository countryRepo;

    @Test
    void shouldHaveIdAndDate_whenCreated() {
        Country createdCountry = countryRepo.save(buildCountry());
        State createdState = stateRepo.save(buildState(createdCountry));
        City createdCity = cityRepo.save(buildCity(createdState, createdCountry));
        Address address = buildAddress(createdCity, createdState, createdCountry);

        Customer aCustomer = buildCustomer(address);

        Customer createdCustomer = testee.save(aCustomer);

        Customer retrievedCustomer = testee.findById(createdCustomer.getId()).orElse(new Customer());

        assertAll(
                () -> assertThat(createdCustomer.getId(), is(notNullValue())),
                () -> assertThat(createdCustomer.getCreatedAt(), is(notNullValue())),
                () -> assertThat(createdCustomer.getUpdatedAt(), is(notNullValue())),
                () -> assertThat(createdCustomer, is(retrievedCustomer)),
                () -> assertThat(createdCustomer.getName(), is(retrievedCustomer.getName())),
                () -> assertThat(createdCustomer.getAddress(), is(retrievedCustomer.getAddress()))
        );
    }

    private Customer buildCustomer(Address address) {
        return Customer.builder()
                .name("CustomerName")
                .surname("CustomerSurname")
                .email("customer@bestinsurance.com")
                .address(address)
                .build();
    }

    private Address buildAddress(City city, State state, Country country) {
        return Address.builder()
                .addressLine("anAddressLine")
                .postalCode("X12 3YZ")
                .city(city)
                .state(state)
                .country(country)
                .build();
    }

    private City buildCity(State state, Country country) {
        return City.builder()
                .name("aCity")
                .population(10000)
                .state(state)
                .country(country)
                .build();
    }

    private State buildState(Country country) {
        return State.builder()
                .name("aState")
                .population(100000)
                .country(country)
                .build();
    }

    private Country buildCountry() {
        return Country.builder()
                .name("aCountry")
                .population(1000000)
                .build();
    }
}
