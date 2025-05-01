package com.bestinsurance.api.common;

import static com.bestinsurance.api.common.AbstractCustomerInitializedTest.cityIds;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceAddress;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceCoverage;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceCustomer;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instancePolicy;
import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instanceSubscription;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractCustomerWithAssociationsTest {

    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected PolicyRepository policyRepository;
    @Autowired
    protected SubscriptionRepository subscriptionRepository;
    @Autowired
    protected CityRepository cityRepository;
    @Autowired
    protected CoverageRepository coverageRepository;

    protected List<Policy> policies = new ArrayList<>();
    protected List<Customer> customers = new ArrayList<>();
    protected List<Coverage> coverages = new ArrayList<>();

    /**
     * Initializes the database with 10 customers
     * 3 customers with policy silver (price 100 - coverages: coverage0, coverage1, coverage2)
     * 7 customers with policy gold (price 200 - coverages: coverage3, coverage4)
     */
    @BeforeAll
    public void createUserSubscriptions() {
        this.clean();
        for (int i = 0; i < 5; i++) {
            this.coverages.add(coverageRepository.save(instanceCoverage("coverage" + i, "description")));
        }

        City city = findCity(cityIds[0]);
        Policy p = instancePolicy("silver", "silver policy", new BigDecimal(100),
                coverages.get(0), coverages.get(1), coverages.get(2));
        Policy p2 = instancePolicy("gold", "gold policy", new BigDecimal(200),
                coverages.get(3), coverages.get(4));
        policies.add(policyRepository.save(p));
        policies.add(policyRepository.save(p2));
        for (int i = 0; i < 3; i++) {
            Customer c = instanceCustomer("customerName" + i, "customerSurname" + i, "customer" + i + "@cus.com",
                    LocalDate.now().minusYears(30), instanceAddress("St John Street", "400-333", city));
            customers.add(customerRepository.save(c));
            Subscription s = instanceSubscription(c, p, new BigDecimal(100), LocalDate.now(), LocalDate.now().plusYears(1));
            subscriptionRepository.save(s);
        }
        for (int i = 3; i < 10; i++) {
            Customer c = instanceCustomer("customerName" + i, "customerSurname" + i, "customer" + i + "@cus.com",
                    LocalDate.now().minusYears(30), instanceAddress("St Peter Street", "500-199", city));
            customers.add(customerRepository.save(c));
            Subscription s = instanceSubscription(c, p2, new BigDecimal(100), LocalDate.now(), LocalDate.now().plusMonths(6));
            subscriptionRepository.save(s);
        }
    }

    @AfterAll
    public void clean() {
        subscriptionRepository.deleteAll();
        policyRepository.deleteAll();
        coverageRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private City findCity(String cityId) {
        return cityRepository.findById(UUID.fromString(cityId)).get();
    }
}
