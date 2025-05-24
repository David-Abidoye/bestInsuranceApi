package com.bestinsurance.api.common;

import com.bestinsurance.api.model.*;
import com.bestinsurance.api.repos.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static com.bestinsurance.api.common.PersistenceEntitiesUtil.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractCustomerSubscriptionStatesTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    protected PolicyRepository policyRepository;
    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    /**
     * Initialize the database creating max 10 user for each state already in database, one for each city,
     * if the states has less than 10 cities, then the number of customers for that state will be equals to the
     * number of the cities.
     * Every user has a subscription paid 100 dollars for the policy silver.
     */
    @BeforeAll
    @Transactional
    public void initializeDB() {
        this.cleanDB();
        Policy silverPolicy = policyRepository.save(instancePolicy("silver", "silver policy", new BigDecimal(100)));
        List<State> states = stateRepository.selectStatesAndCitiesOrdered();
        int customerCount = 0;
        for (State state : states) {
            Iterator<City> iterCity = state.getStateCities().iterator();
            int i = 0;
            while(iterCity.hasNext() && i < 10) {
                Customer c = customerRepository.save(instanceCustomer("customerName" + customerCount, "customerSurname" + customerCount,
                        "customer" + customerCount + "@customer.com", LocalDate.now().minusYears(30),
                        instanceAddress("street test " + 1, "12345" + i, iterCity.next())));
                Subscription s = instanceSubscription(c, silverPolicy, new BigDecimal(100), LocalDate.now(), LocalDate.now().plusYears(1));
                subscriptionRepository.save(s);
                customerCount++;
                i++;
            }
        }
    }

    @AfterAll
    public void cleanDB() {
        subscriptionRepository.deleteAll();
        policyRepository.deleteAll();
        customerRepository.deleteAll();
    }
}