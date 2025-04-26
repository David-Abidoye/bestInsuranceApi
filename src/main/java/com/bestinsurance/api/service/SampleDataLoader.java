package com.bestinsurance.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.State;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;
import jakarta.annotation.PostConstruct;

public class SampleDataLoader {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CoverageRepository coverageRepository;
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    private final Random random = new Random();

    @PostConstruct
    public void populateDatabase(){
        if (customerRepository.count() == 0){
            List<Customer> customers = createCustomers();
            List<Policy> policies = createPolicies();
            createSubscriptions(customers, policies);
        }
    }

    private List<Customer> createCustomers(){
        String[] names = {"William", "Elizabeth", "James", "Mary", "John", "Sarah", "Michael", "Emily", "David", "Jessica"};
        String[] surnames = {"Smith", "Johnson", "Brown", "Davis", "Garcia", "Wilson", "Jones", "Jackson", "Anderson", "Harris"};
        String[] streetNames = {"Oak", "Maple", "Pine", "Cedar", "Elm", "Main", "High", "Park", "Washington", "Adams"};
        String[] cityIds = {"45576d7c-8d84-4422-9440-19ef80fa16f3",
                "91f360d5-811b-417c-a202-f5ba4b34b895",
                "144b05b6-ebf6-43a8-836d-0998c2c20a3c",
                "74716a04-d538-4441-84bf-7c41470778ca",
                "eb5e9505-8580-4857-9195-6bee0324ac0f"};

        LocalDate[] birthDates = {LocalDate.now().minusYears(20),
                LocalDate.now().minusYears(40),
                LocalDate.now().minusYears(50),
                LocalDate.now().minusYears(60),
                LocalDate.now().minusYears(70)};

        List<Customer> customers = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            customer.setName(names[i]);
            customer.setSurname(surnames[i]);
            customer.setBirthDate(birthDates[i/2]);
            customer.setEmail(customer.getName().toLowerCase() + "." + customer.getSurname().toLowerCase() + "@example.com");
            customer.setTelephoneNumber(String.format("(%d)%d-%04d", random.nextInt(900) + 100, random.nextInt(900) + 100, random.nextInt(10000)));
            City city = findCity(cityIds[i/2]);
            Address a = new Address();
            a.setCity(city);
            State s = new State();
            s.setId(city.getState().getId());
            a.setState(s);
            Country co = new Country();
            co.setId(city.getCountry().getId());
            a.setCountry(co);
            a.setAddressLine(streetNames[i] + " Street " + random.nextInt(100));
            customer.setAddress(a);
            customers.add(customerRepository.save(customer));
        }
        return customers;
    }

    private City findCity(String cityId) {
        return cityRepository.findById(UUID.fromString(cityId)).orElseThrow(() -> new RuntimeException("City with id: " + cityId + " does not exist in database"));
    }

    private List<Policy> createPolicies() {
        List<Policy> policies = new ArrayList<>();
        int[][] coveragesIndexes = {{0,0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}};
        List<Coverage> coverages = createCoverages();

        String[] policyNames = {"Basic", "Bronze", "Silver", "Gold", "Platinum"};
        String[] policyDescriptions = {"Basic Policy", "Bronze Policy", "Silver Policy", "Gold Policy", "Platinum Policy"};
        BigDecimal[] policyPrices = {new BigDecimal("100.00"), new BigDecimal("250.00"), new BigDecimal("300.00"),
                new BigDecimal("500.00"), new BigDecimal("1000.00")};

        for (int i = 0; i < 5; i++) {
            Policy policy = new Policy();
            policy.setName(policyNames[i]);
            policy.setDescription(policyDescriptions[i]);
            policy.setPrice(policyPrices[i]);
            Set<Coverage> pcov = new HashSet<>();
            for (int j = coveragesIndexes[i][0]; j <= coveragesIndexes[i][1]; j++) {
                pcov.add(coverages.get(j));
            }
            policy.setCoverages(pcov);
            policies.add(policyRepository.save(policy));
        }

        return policies;
    }

    private List<Coverage> createCoverages(){
        List<Coverage> coverages = new ArrayList<>();
        Map<String, String> coverageInfo = new HashMap<>();
        coverageInfo.put("Liability", "Provides protection against claims resulting from injuries and damage to people or property caused by you or your employees.");
        coverageInfo.put("Collision", "Covers damage to your vehicle caused by a collision with another vehicle or object.");
        coverageInfo.put("Comprehensive", "Covers damage to your vehicle caused by something other than a collision, such as theft, vandalism, or natural disasters.");
        coverageInfo.put("Personal Injury", "Covers medical expenses and lost wages for you and your passengers in case of an accident, regardless of who is at fault.");
        coverageInfo.put("Uninsured", "Covers damages and injuries caused by a driver who has no insurance or not enough insurance to cover the damages.");

        for (Map.Entry<String, String> entry : coverageInfo.entrySet()) {
            Coverage coverage = new Coverage();
            coverage.setName(entry.getKey());
            coverage.setDescription(entry.getValue());
            coverages.add(coverageRepository.save(coverage));
        }
        return coverages;
    }
    private void createSubscriptions(List<Customer> customers, List<Policy> policies) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            Policy policy = policies.get(i/2);
            SubscriptionId id = new SubscriptionId();
            id.setCustomerId(customer.getId());
            id.setPolicyId(policy.getId());

            Subscription subscription = new Subscription();
            subscription.setId(id);
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(LocalDate.now().plusYears(1));
            subscription.setPaidPrice(policy.getPrice());

            subscription.setCustomer(customer);
            subscription.setPolicy(policy);

            subscriptionRepository.save(subscription);
        }
    }
}
