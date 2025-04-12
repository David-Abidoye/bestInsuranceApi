package com.bestinsurance.api.jpa;

import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceCity;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceCountry;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceCustomer;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
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
class CustomerJpaTest {

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
        Country country = instanceCountry("aCountry", 1000000);
        State state = instanceState("aState", 100000, country);
        City city = instanceCity("aCity", 10000, state, country);

        Country createdCountry = countryRepo.save(country);
        State createdState = stateRepo.save(state);
        City createdCity = cityRepo.save(city);
        Address address = buildAddress(createdCity, createdState, createdCountry);

        Customer aCustomer = instanceCustomer("customerName", "customerSurname", "name.surname@customer.com", address);

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

    private Address buildAddress(City city, State state, Country country) {
        return Address.builder()
                .addressLine("anAddressLine")
                .postalCode("X12 3YZ")
                .city(city)
                .state(state)
                .country(country)
                .build();
    }
}
