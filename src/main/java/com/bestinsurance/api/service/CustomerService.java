package com.bestinsurance.api.service;

import static com.bestinsurance.api.helper.AddressHelper.areAllAddressFieldsPresent;
import static com.bestinsurance.api.helper.AddressHelper.isAnyAddressFieldPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
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
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService extends AbstractCrudService<Customer, UUID> {

    private static final String BIRTH_DATE = "birthDate";

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

    public List<Customer> findAllWithFilters(String name, String surname, String email, Integer ageFrom,
                                             Integer ageTo, CustomerOrderBy orderBy, Sort.Direction orderDirection) {

        Example<Customer> customerExample = buildCustomerExample(name, surname, email);

        Specification<Customer> customerSpecification = buildCustomerSpecification(customerExample, ageFrom, ageTo);

        Sort sort = getSortOrder(orderBy, orderDirection);

        return getRepository().findAll(customerSpecification, sort);
    }

    public List<Customer> findAllCustomersByPolicyId(UUID policyId) {
        return getRepository().findAllCustomersByPolicyId(policyId);
    }

    public List<Customer> findAllCustomersByCoverageId(UUID coverageId) {
        return getRepository().findAllCustomersByCoverageId(coverageId);
    }

    public List<Customer> findAllCustomersWithDiscountedPrice() {
        return getRepository().findAllCustomersWithDiscount();
    }

    public List<Customer> findAllCustomersWithActiveSubscriptionBetweenDates(LocalDate startDate, LocalDate endDate) {
        return getRepository().findAllCustomersWithActiveSubscriptionBetween(startDate, endDate);
    }

    private Example<Customer> buildCustomerExample(String name, String surname, String email) {
        Customer probe = Customer.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("createdAt", "updatedAt")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return Example.of(probe, matcher);
    }

    private Specification<Customer> buildCustomerSpecification(Example<Customer> customerExample, Integer ageFrom, Integer ageTo) {

        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, criteriaBuilder, customerExample);

            if (examplePredicate != null) {
                predicates.add(examplePredicate);
            }

            LocalDate today = LocalDate.now();
            if (ageFrom != null) {
                LocalDate maxBirthDate = today.minusYears(ageFrom);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(BIRTH_DATE), maxBirthDate));
            }

            if (ageTo != null) {
                LocalDate minBirthDate = today.minusYears(ageTo);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(BIRTH_DATE), minBirthDate));
            }

            return predicates.isEmpty() ?
                    criteriaBuilder.conjunction() :
                    criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        });
    }

    private Sort getSortOrder(CustomerOrderBy orderBy, Sort.Direction orderDirection) {
        if (orderBy == null) {
            return Sort.by(Sort.Direction.ASC, CustomerOrderBy.NAME.getValue());
        }

        Sort.Direction direction = getSortDirection(orderDirection);
        String property = orderBy.getValue();
        Sort sortOrder = Sort.by(direction, property);

        if (orderBy == CustomerOrderBy.AGE) {
            return sortOrder.reverse();
        }

        return sortOrder;
    }

    private Sort.Direction getSortDirection(Sort.Direction orderDirection) {
        return orderDirection == null ? Sort.DEFAULT_DIRECTION : orderDirection;
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

    @Getter
    public enum CustomerOrderBy {
        NAME("name"),
        SURNAME("surname"),
        EMAIL("email"),
        AGE(BIRTH_DATE);

        private final String value;

        CustomerOrderBy(String value) {
            this.value = value;
        }
    }
}
