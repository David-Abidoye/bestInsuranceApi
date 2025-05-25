package com.bestinsurance.api.controller;

import static com.bestinsurance.api.common.PersistenceEntitiesUtil.instancePolicy;
import static com.bestinsurance.api.security.Roles.BACK_OFFICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import com.bestinsurance.api.common.PersistenceEntitiesUtil;
import com.bestinsurance.api.model.Address;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.repos.AddressRepository;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvUploadTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MockMvc mockMvc;
    @LocalServerPort
    private int port;

    @BeforeAll
    void initDB() {
        Policy silver = instancePolicy("Silver", "Silver Policy", new BigDecimal(100));
        Policy gold = instancePolicy("Gold", "Gold Policy", new BigDecimal(200));
        Policy platinum = instancePolicy("Platinum", "Silver Policy", new BigDecimal(300));

        policyRepository.save(silver);
        policyRepository.save(gold);
        policyRepository.save(platinum);
    }

    @AfterAll
    void cleanDB() {
        policyRepository.deleteAll();
    }

    @AfterEach
    void cleanSingleTestDB() {
        subscriptionRepository.deleteAll();
        customerRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void testCsvUpload() throws Exception {
        ClassPathResource res = new ClassPathResource("customers.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(user("back_office_user").roles(BACK_OFFICE)))
                .andExpect(status().isOk());
        List<Customer> customers = customerRepository.findAll();
        assertEquals(500, customers.size());
    }

    @Test
    void noCityError() throws Exception {
        ClassPathResource res = new ClassPathResource("customers_nocityerror.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(user("back_office_user").roles(BACK_OFFICE)))
                .andExpect(status().isNotFound());
        List<Customer> customers = customerRepository.findAll();
        assertEquals(0, customers.size());
    }

    @Test
    void noStateError() throws Exception {
        ClassPathResource res = new ClassPathResource("customers_nostateerror.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(user("back_office_user").roles(BACK_OFFICE)))
                .andExpect(status().isNotFound());
        List<Customer> customers = customerRepository.findAll();
        assertEquals(0, customers.size());
    }

    @Test
    void existingEmail() throws Exception {
        final String email = "alvinhoke@hotmail.com";
        final String newCityId = "035c1dbb-6f5e-48c4-a6f7-22abf17a9f7d";
        final String newPostalCode = "30114";
        Address address = PersistenceEntitiesUtil.instanceAddress("OLD ADDRESS", "OLDPostalCode",
                cityRepository.findById(UUID.fromString("3d8dfce1-49bc-4ad7-88b5-f4294c410fba")).get());
        Customer customer = PersistenceEntitiesUtil.instanceCustomer("Matha", "Loera", email,
                LocalDate.parse("1957-01-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")), address);
        customerRepository.save(customer);

        ClassPathResource res = new ClassPathResource("customers_emailtest.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(user("back_office_user").roles(BACK_OFFICE)))
                .andExpect(status().isOk());
        List<Customer> customers = customerRepository.findAll();
        int size = 0;
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                assertEquals("4203 Parsonage Circle", c.getAddress().getAddressLine());
                assertEquals(newCityId, c.getAddress().getCity().getId().toString());
                assertEquals(newPostalCode, c.getAddress().getPostalCode());
            }
            size++;
        }
        assertEquals(3, size);
    }

    //TODO to use authenticated user with restassured test as done for other tests with MockUser
    //@Test
    //void testExceedMaxSize() throws IOException {
    //    ClassPathResource resource = new ClassPathResource("customers_toobig.csv");
    //    File file = resource.getFile();
    //    RestAssured.baseURI = "http://localhost/subscriptions";
    //    RestAssured.port = port;
    //    Response response = given()
    //            .multiPart(file)
    //            .when().post("/upload");
    //
    //    assertThat(response.getStatusCode(), is(500));
    //}
}
