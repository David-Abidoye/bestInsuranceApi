package com.bestinsurance.api.controller;

import static com.bestinsurance.api.helper.ConstraintHelper.DATE_PATTERN;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parseCustomerOrderByFilter;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parseIntegerFilter;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parseOrderDirection;
import static com.bestinsurance.api.validation.utils.ValidationUtils.parseStringFilter;
import static com.bestinsurance.api.validation.utils.ValidationUtils.validateAgeFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bestinsurance.api.dto.CustomerCreateRequest;
import com.bestinsurance.api.dto.CustomerResponse;
import com.bestinsurance.api.dto.CustomerUpdateRequest;
import com.bestinsurance.api.mapper.CustomerCreateMapper;
import com.bestinsurance.api.mapper.CustomerResponseMapper;
import com.bestinsurance.api.mapper.CustomerUpdateMapper;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Valid
@RestController
@RequestMapping("/customers")
public class CustomerController extends AbstractSimpleIdCrudController<CustomerCreateRequest, CustomerUpdateRequest, CustomerResponse, Customer> {

    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email";
    public static final String AGE = "age";
    public static final String AGE_FROM = "ageFrom";
    public static final String AGE_TO = "ageTo";
    public static final String ORDER_BY = "orderBy";
    public static final String ORDER_DIRECTION = "orderDirection";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String PAGE_SIZE = "pageSize";

    private final CustomerService customerService;
    private final CustomerCreateMapper customerCreateMapper;
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerUpdateMapper customerUpdateMapper;

    public CustomerController(CustomerService customerService,
                              CustomerCreateMapper customerCreateMapper,
                              CustomerResponseMapper customerResponseMapper,
                              CustomerUpdateMapper customerUpdateMapper) {
        this.customerService = customerService;
        this.customerCreateMapper = customerCreateMapper;
        this.customerResponseMapper = customerResponseMapper;
        this.customerUpdateMapper = customerUpdateMapper;
    }

    @Override
    protected CustomerService getService() {
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
    protected CustomerResponseMapper getSearchDtoMapper() {
        return customerResponseMapper;
    }

    @Override
    @GetMapping(value = "/{id}", produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public CustomerResponse searchById(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        Customer foundCustomer = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + id + " does not exist!"));
        return getSearchDtoMapper().map(foundCustomer);
    }

    @Override
    @Parameter(in = ParameterIn.QUERY, name = NAME, description = "filters all the customers with a name containing a string", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.QUERY, name = SURNAME, description = "filters all the customers with a surname containing a string", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.QUERY, name = EMAIL, description = "filters all the customers with an email containing a string", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.QUERY, name = AGE_FROM, description = "filters all the customers by age interval, the starting age", example = "20", schema = @Schema(type = "number"))
    @Parameter(in = ParameterIn.QUERY, name = AGE_TO, description = "filters all the customers by age interval, the ending age", example = "40", schema = @Schema(type = "number"))
    @Parameter(in = ParameterIn.QUERY, name = ORDER_BY, description = "a string parameter that indicates which field should be used for sorting", schema = @Schema(type = "string", allowableValues = {NAME, SURNAME, EMAIL, AGE}))
    @Parameter(in = ParameterIn.QUERY, name = ORDER_DIRECTION, description = "a string parameter that indicates which in which direction to sort by", schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
    @Parameter(in = ParameterIn.QUERY, name = PAGE_NUMBER, description = "the number of the page to return", example = "2", schema = @Schema(type = "number"))
    @Parameter(in = ParameterIn.QUERY, name = PAGE_SIZE, description = "the size of the page to return", example = "5", schema = @Schema(type = "number", defaultValue = "10"))
    public List<CustomerResponse> all(Map<String, String> filters) {

        String name = parseStringFilter(filters, NAME);
        String surname = parseStringFilter(filters, SURNAME);
        String email = parseStringFilter(filters, EMAIL);
        Integer ageFrom = parseIntegerFilter(filters, AGE_FROM);
        Integer ageTo = parseIntegerFilter(filters, AGE_TO);
        validateAgeFilter(ageFrom, ageTo);
        CustomerService.CustomerOrderBy orderBy = parseCustomerOrderByFilter(filters.get(ORDER_BY));
        Sort.Direction orderDirection = parseOrderDirection(filters.get(ORDER_DIRECTION));
        Integer pageNumber = parseIntegerFilter(filters, PAGE_NUMBER);
        Integer pageSize = Optional.ofNullable(parseIntegerFilter(filters, PAGE_SIZE)).orElse(10);

        List<Customer> allDomainObjects = getService().findAllWithFilters(name, surname, email, ageFrom, ageTo, orderBy, orderDirection, pageNumber, pageSize);
        return getSearchDtoMapper().mapCustomers(allDomainObjects);
    }

    @GetMapping("/policy/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "to search all the customers that subscribe to a certain policy (by id policy)", schema = @Schema(type = "string"), required = true)
    public List<CustomerResponse> allByPolicyId(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        List<Customer> foundCustomers = getService().findAllCustomersByPolicyId(id);
        return getSearchDtoMapper().mapCustomers(foundCustomers);
    }

    @GetMapping("/coverage/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "to search all the customers that have a certain coverage (by id coverage)", schema = @Schema(type = "string"), required = true)
    public List<CustomerResponse> allByCoverageId(@PathVariable Map<String, String> idDTO) {
        UUID id = getIdMapper().map(idDTO);
        List<Customer> foundCustomers = getService().findAllCustomersByCoverageId(id);
        return getSearchDtoMapper().mapCustomers(foundCustomers);
    }

    @GetMapping("/subscriptions/discountedPrice")
    public List<CustomerResponse> allWithDiscountedPrice() {
        List<Customer> foundCustomers = getService().findAllCustomersWithDiscountedPrice();
        return getSearchDtoMapper().mapCustomers(foundCustomers);
    }

    @GetMapping("/subscriptions")
    @Parameter(in = ParameterIn.QUERY, name = "startDate", description = "to search all the customers with an active subscription between two provided dates(startDate)", schema = @Schema(type = "string"), required = true)
    @Parameter(in = ParameterIn.QUERY, name = "endDate", description = "to search all the customers with an active subscription between two provided dates(endDate)", schema = @Schema(type = "string"), required = true)
    public List<CustomerResponse> allWithActiveSubscription(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_PATTERN) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_PATTERN) LocalDate endDate) {
        List<Customer> foundCustomers = getService().findAllCustomersWithActiveSubscriptionBetweenDates(startDate, endDate);
        return getSearchDtoMapper().mapCustomers(foundCustomers);
    }
}
