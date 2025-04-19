package com.bestinsurance.api.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.CustomerCreateRequest;
import com.bestinsurance.api.dto.CustomerResponse;
import com.bestinsurance.api.dto.CustomerUpdateRequest;
import com.bestinsurance.api.mapper.CustomerCreateMapper;
import com.bestinsurance.api.mapper.CustomerResponseMapper;
import com.bestinsurance.api.mapper.CustomerUpdateMapper;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.service.CrudService;
import com.bestinsurance.api.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractSimpleIdCrudController<CustomerCreateRequest, CustomerUpdateRequest, CustomerResponse, Customer> {

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
    protected CrudService<Customer, UUID> getService() {
        return customerService;
    }

    @Override
    protected DTOMapper<CustomerCreateRequest, Customer> getCreateDtoMapper() {
        return customerCreateMapper;
    }

    @Override
    protected DTOMapper<CustomerUpdateRequest, Customer> getUpdateDtoMapper() {
        return customerUpdateMapper;
    }

    @Override
    protected DTOMapper<Customer, CustomerResponse> getSearchDtoMapper() {
        return customerResponseMapper;
    }

    @Override
    @GetMapping("/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public CustomerResponse searchById(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        Customer foundCustomer = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + id + " does not exist!"));
        return customerResponseMapper.map(foundCustomer);
    }
}
