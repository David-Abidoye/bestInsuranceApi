package com.bestinsurance.api.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.bestinsurance.api.dto.SubscriptionCreateRequest;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.City;
import com.bestinsurance.api.model.Country;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.model.State;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionCRUDControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CoverageRepository coverageRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MockMvc mockMvc;

    private Customer customer;
    private Policy policy;

    private Subscription subscription;


    @BeforeAll
    void init() {
        om.registerModule(new JavaTimeModule());
        this.customer = createTestCustomer();
        this.policy = createTestPolicy("init");
        this.subscription = createSubscription(this.customer, this.policy);
    }

    @AfterAll
    void cleanup() {
        this.subscriptionRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.policyRepository.deleteAll();
    }

    @Test
    void testCreateSubscription() throws Exception  {
        SubscriptionCreateRequest subscriptionDTO = new SubscriptionCreateRequest();
        subscriptionDTO.setCustomerId(this.customer.getId().toString());
        // Let's create a new policy to create a new subscription
        Policy testPolicy = createTestPolicy("testCreate");
        subscriptionDTO.setPolicyId(testPolicy.getId().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(LocalDate.now().plusYears(1));
        subscriptionDTO.setPaidPrice(new BigDecimal(100.00));
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(subscriptionDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer.id", notNullValue()))
                .andExpect(jsonPath("$.policy.id", notNullValue()))
                .andReturn();
    }

    @Test
    void testFindById() throws Exception {
         mockMvc.perform(get("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getId().toString()
                         , this.subscription.getPolicy().getId().toString())
                 .contentType(MediaType.APPLICATION_JSON)
                 .queryParam("idCustomer", this.customer.getId().toString())
                 .queryParam("idPolicy", this.policy.getId().toString()))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.customer.id", notNullValue()))
                 .andExpect(jsonPath("$.policy.id", notNullValue()));
    }

    @Test
    void testUpdate() throws Exception {
        LocalDate updateDate = LocalDate.now().plusYears(3);
        String formattedDateTime = updateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        SubscriptionCreateRequest subscriptionDTO = new SubscriptionCreateRequest();
        subscriptionDTO.setCustomerId(this.customer.getId().toString());
        subscriptionDTO.setPolicyId(this.policy.getId().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(updateDate);
        subscriptionDTO.setPaidPrice(new BigDecimal(150));

        mockMvc.perform(put("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getId().toString()
                        , this.subscription.getPolicy().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCustomer", this.customer.getId().toString())
                        .queryParam("idPolicy", this.policy.getId().toString())
                        .content(om.writeValueAsString(subscriptionDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id", notNullValue()))
                .andExpect(jsonPath("$.policy.id", notNullValue()))
                .andExpect(jsonPath("$.paidPrice", is(150)))
                .andExpect(jsonPath("$.endDate", is(formattedDateTime)));
    }

    @Test
    void testDelete() throws Exception {
        Policy testPolicy = createTestPolicy("testDElete");
        Subscription subscription1 = createSubscription(this.customer, testPolicy);
        mockMvc.perform(delete("/subscriptions/{idCustomer}/{idPolicy}", subscription1.getCustomer().getId().toString()
                        , subscription1.getPolicy().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCustomer", customer.getId().toString())
                        .queryParam("idPolicy", testPolicy.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    private Customer createTestCustomer(){
        Customer customer = new Customer();
        customer.setName("testNameSubs");
        customer.setSurname("testSurnameSubs");
        customer.setEmail("testEmailSubs@email.com");
        customer.setBirthDate(LocalDate.now().minusYears(30));
        Address address = new Address();
        address.setAddressLine("123 Test Street, APT 4");
        address.setPostalCode("12345-44");
        Country country = new Country();
        country.setId(UUID.fromString("d4153ed2-91e6-40da-a3c5-1de8a6d0119c"));
        address.setCountry(country);
        State state = new State();
        state.setId(UUID.fromString("4b62177f-7eb0-448e-86c1-3e168e44cc29"));
        address.setState(state);
        City city = new City();
        city.setId(UUID.fromString("45576d7c-8d84-4422-9440-19ef80fa16f3"));
        address.setCity(city);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }
    //SAve and return result
    private Policy createTestPolicy(String name) {
        Set<Coverage> coverages = new LinkedHashSet<>();
        for (int i=0; i <= 2; i++) {
            Coverage coverage = new Coverage();
            coverage.setName("testCoverage" + i);
            coverage.setDescription("subs coverage description " + i);
            coverages.add(coverageRepository.save(coverage));
        }

        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription("policy test subscriptions description");
        policy.setPrice(new BigDecimal(100.50));
        policy.setCoverages(coverages);

        return policyRepository.save(policy);
    }

    private Subscription createSubscription(Customer c, Policy p) {
        SubscriptionId id = new SubscriptionId();
        id.setCustomerId(c.getId());
        id.setPolicyId(p.getId());
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        subscription.setPaidPrice(new BigDecimal(100.00));
        subscription.setCustomer(c);
        subscription.setPolicy(p);
        return subscriptionRepository.save(subscription);
    }

}