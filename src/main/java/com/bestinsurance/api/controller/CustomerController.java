package com.bestinsurance.api.controller;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.CustomerCreateRequest;
import com.bestinsurance.api.dto.CustomerResponse;
import com.bestinsurance.api.dto.CustomerUpdateRequest;
import com.bestinsurance.api.mapper.CustomerCreateMapper;
import com.bestinsurance.api.mapper.CustomerResponseMapper;
import com.bestinsurance.api.mapper.CustomerUpdateMapper;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerController implements CrudController<CustomerCreateRequest, CustomerUpdateRequest, CustomerResponse> {

    private final CustomerService customerService;
    private final CustomerCreateMapper customerCreateMapper;
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerUpdateMapper customerUpdateMapper;

    public CustomerController(CustomerService customerService, CustomerCreateMapper customerCreateMapper, CustomerResponseMapper customerResponseMapper, CustomerUpdateMapper customerUpdateMapper) {
        this.customerService = customerService;
        this.customerCreateMapper = customerCreateMapper;
        this.customerResponseMapper = customerResponseMapper;
        this.customerUpdateMapper = customerUpdateMapper;
    }

    @Override
    public CustomerResponse create(CustomerCreateRequest customerCreateRequest) {
        Customer createdCustomer = customerService.create(customerCreateMapper.map(customerCreateRequest));
        return customerResponseMapper.map(createdCustomer);
    }

    @Override
    public CustomerResponse searchById(String id) {
        Customer foundCustomer = customerService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + id + " does not exist!"));
        return customerResponseMapper.map(foundCustomer);
    }

    @Override
    public List<CustomerResponse> all() {
        List<Customer> allCustomers = customerService.findAll();
        return allCustomers.stream()
                .map(customerResponseMapper::map)
                .toList();
    }

    @Override
    public CustomerResponse update(String id, CustomerUpdateRequest customerUpdateRequest) {
        Customer updatedCustomer = customerService.update(id, customerUpdateMapper.map(customerUpdateRequest));
        return customerResponseMapper.map(updatedCustomer);
    }

    @Override
    public void delete(String id) {
        customerService.delete(id);
    }
}
