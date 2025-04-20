package com.bestinsurance.api.service;

import static com.bestinsurance.api.helper.AddressHelper.areAllAddressFieldsPresent;
import static com.bestinsurance.api.helper.AddressHelper.isAnyAddressFieldPresent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.State;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CountryRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.StateRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService extends AbstractCrudService<Customer, UUID> {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    public CustomerService(CustomerRepository customerRepository, CityRepository cityRepository, StateRepository stateRepository, CountryRepository countryRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    protected CustomerRepository getRepository() {
        return customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        String email = customer.getEmail();
        if (getRepository().existsByEmail(email)) {
            log.debug("Customer with email {} already exists", email);
            throw new EntityExistsException("Customer with email " + email + " already exists");
        } else {
            log.debug("Saving a new customer");
            enrichAddress(customer.getAddress());
            return getRepository().save(customer);
        }
    }

    @Override
    public List<Customer> findAll() {
        log.debug("Fetching list of all customers");
        return super.findAll();
    }

    @Override
    public Optional<Customer> getById(UUID id) {
        log.debug("Fetching customer with id: {}", id);
        return super.getById(id);
    }

    @Override
    public Customer update(UUID id, Customer customer) {
        log.debug("Updating customer with id: {}", id);
        return getRepository().findById(id)
                .map(customerToUpdate -> {
                    updateCustomerFields(customerToUpdate, customer);
                    return getRepository().save(customerToUpdate);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Customer with id: %s does not exist!", id)));
    }

    @Override
    public void delete(UUID id) {
        log.debug("Deleting customer with id: {}", id);
        super.delete(id);
    }

    private void updateCustomerFields(Customer customerToUpdate, Customer customer) {
        Optional.ofNullable(customer.getEmail()).ifPresent(email -> {
            if (getRepository().existsByEmail(email)) {
                log.debug("Email {} already exists", email);
                throw new EntityExistsException("Cannot update! Email: " + email + " already exists");
            } else {
                customerToUpdate.setEmail(email);
            }
        });

        Optional.ofNullable(customer.getTelephoneNumber()).ifPresent(customerToUpdate::setTelephoneNumber);
        Optional.ofNullable(customer.getUpdatedAt()).ifPresent(customerToUpdate::setUpdatedAt);
        Optional.ofNullable(customer.getAddress()).ifPresent(address -> {
            if (!isAnyAddressFieldPresent(address)) {
                return;
            }
            if (areAllAddressFieldsPresent(address)) {
                enrichAddress(address);
                customerToUpdate.setAddress(address);
            } else {
                throw new IllegalArgumentException("If updating address at all, all fields must be provided.");
            }
        });
    }

    private void enrichAddress(Address address) {
        UUID cityId = address.getCity().getId();
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("City with id: %s not found", cityId)));
        UUID stateId = address.getState().getId();
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("State with id: %s not found", stateId)));
        UUID countryId = address.getCountry().getId();
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Country with id: %s not found", countryId)));

        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
    }
}
