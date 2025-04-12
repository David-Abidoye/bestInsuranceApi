package com.bestinsurance.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService implements CrudService<Customer> {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        log.debug("Saving a new customer");
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        log.debug("Fetching list of all customers");
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(String id) {
        log.debug("Fetching customer with id: {}", id);
        return customerRepository.findById(UUID.fromString(id));
    }

    @Override
    public Customer getReferenceById(String id) {
        return customerRepository.getReferenceById(UUID.fromString(id));
    }

    @Override
    public Customer update(String id, Customer customer) {
        log.debug("Updating customer with id: {}", id);
        return customerRepository.findById(UUID.fromString(id))
                .map(customerToUpdate -> {
                    updateCustomerFields(customerToUpdate, customer);
                    return customerRepository.save(customerToUpdate);
                })
                .orElseThrow(() -> new NoSuchElementException("Customer with id: " + id + " does not exist!"));
    }

    @Override
    public void delete(String id) {
        log.debug("Deleting customer with id: {}", id);
        customerRepository.deleteById(UUID.fromString(id));
    }

    private void updateCustomerFields(Customer customerToUpdate, Customer customer) {
        Optional.ofNullable(customer.getEmail()).ifPresent(customerToUpdate::setEmail);
        Optional.ofNullable(customer.getTelephoneNumber()).ifPresent(customerToUpdate::setTelephoneNumber);
        Optional.ofNullable(customer.getUpdatedAt()).ifPresent(customerToUpdate::setUpdatedAt);
        Optional.ofNullable(customer.getAddress()).ifPresent(customerToUpdate::setAddress);
    }
}
