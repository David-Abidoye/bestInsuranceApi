package com.bestinsurance.api.controller;

import static com.bestinsurance.api.security.Roles.ADMIN;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Comparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.bestinsurance.api.common.AbstractCustomerInitializedTest;
import com.bestinsurance.api.dto.CustomerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerFiltersTest extends AbstractCustomerInitializedTest {

    private static final ObjectMapper om = new ObjectMapper();
    private static final String NAME = "NAME";
    private static final String SURNAME = "SURNAME";
    private static final String EMAIL = "EMAIL";
    private static final String AGE = "AGE";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void init() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    void testAllFiltersInitializedOrderByNameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(NAME).setOrderDirection(ASC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByName() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(NAME).runTest(3);
    }

    @Test
    void testAllFiltersInitialized() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByNameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(NAME).setOrderDirection(DESC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderBySurnameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(SURNAME).setOrderDirection(ASC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderBySurname() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(SURNAME).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderBySurnameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(SURNAME).setOrderDirection(DESC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByAgeASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(AGE).setOrderDirection(ASC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByAge() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(AGE).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByAgeDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(AGE).setOrderDirection(DESC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByEmailASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(EMAIL).setOrderDirection(ASC).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByEmail() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(EMAIL).runTest(3);
    }

    @Test
    void testAllFiltersInitializedOrderByEmailDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(EMAIL).setOrderDirection(DESC).runTest(3);
    }

    @Test
    void testSearchByNameOrderByAgeASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").runTest(5);
    }

    @Test
    void testAgeFromTo() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("30").setAgeTo("60").runTest(30);
    }

    @Test
    void testAge() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("60").setAgeTo("60").runTest(10);
    }

    @Test
    void testEmail() throws Exception {
        new CustomerSearchTestHelper().setEmail("AN").runTest(5);
    }

    @Test
    void testPageNumberWithNoPageSize() throws Exception {
        new CustomerSearchTestHelper().setPageNumber("1").runTest(10);
    }

    @Test
    void testPageNumberAndPageSize() throws Exception {
        new CustomerSearchTestHelper().setPageNumber("1").setPageSize("20").runTest(20);
    }

    private class CustomerSearchTestHelper {
        private String name;
        private String surname;
        private String email;
        private String ageFrom;
        private String ageTo;
        private String orderBy;
        private String orderDirection;
        private String pageNumber;
        private String pageSize;

        private void runTest(int expectedResults) throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/customers")
                            .with(user("admin").roles(ADMIN))
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam(CustomerController.NAME, name)
                            .queryParam(CustomerController.SURNAME, surname)
                            .queryParam(CustomerController.EMAIL, email)
                            .queryParam(CustomerController.AGE_FROM, ageFrom)
                            .queryParam(CustomerController.AGE_TO, ageTo)
                            .queryParam(CustomerController.ORDER_BY, orderBy)
                            .queryParam(CustomerController.ORDER_DIRECTION, orderDirection)
                            .queryParam(CustomerController.PAGE_NUMBER, pageNumber)
                            .queryParam(CustomerController.PAGE_SIZE, pageSize))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedResults)))
                    .andReturn();
            boolean ascending = orderDirection == null || !orderDirection.equals(DESC);

            Comparator<CustomerResponse> comparator;
            if (orderBy == null) {
                comparator = customerCompareByName();
            } else {
                switch (orderBy) {
                    case CustomerController.NAME -> comparator = customerCompareByName();
                    case CustomerController.SURNAME -> comparator = customerCompareBySurname();
                    case CustomerController.EMAIL -> comparator = customerCompareByEmail();
                    case CustomerController.AGE -> comparator = customerCompareByBirthDate();
                    default -> comparator = customerCompareByName();
                }
            }
            assertTrue(this.checkCustomersOrder(om.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse[].class),
                    comparator, ascending));
        }

        private boolean checkCustomersOrder(CustomerResponse[] customers, Comparator<CustomerResponse> comparator, boolean ascendingOrder) {
            for (int i = 1; i < customers.length; i++) {
                if (ascendingOrder) {
                    if (comparator.compare(customers[i - 1], customers[i]) == 1) {
                        return false;
                    }
                } else {
                    if (comparator.compare(customers[i - 1], customers[i]) == -1) {
                        return false;
                    }
                }
            }
            return true;
        }

        private Comparator<CustomerResponse> customerCompareByName() {
            return (CustomerResponse c1, CustomerResponse c2) -> CharSequence.compare(c1.getName(), c2.getName());
        }

        private Comparator<CustomerResponse> customerCompareBySurname() {
            return (CustomerResponse c1, CustomerResponse c2) -> CharSequence.compare(c1.getSurname(), c2.getSurname());
        }

        private Comparator<CustomerResponse> customerCompareByEmail() {
            return (CustomerResponse c1, CustomerResponse c2) -> CharSequence.compare(c1.getEmail(), c2.getEmail());
        }

        private Comparator<CustomerResponse> customerCompareByBirthDate() {
            return Comparator.comparing(CustomerResponse::getBirthDate);
        }

        public CustomerSearchTestHelper setName(String name) {
            this.name = name;
            return this;
        }

        public CustomerSearchTestHelper setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerSearchTestHelper setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerSearchTestHelper setAgeFrom(String ageFrom) {
            this.ageFrom = ageFrom;
            return this;
        }

        public CustomerSearchTestHelper setAgeTo(String ageTo) {
            this.ageTo = ageTo;
            return this;
        }

        public CustomerSearchTestHelper setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public CustomerSearchTestHelper setOrderDirection(String orderDirection) {
            this.orderDirection = orderDirection;
            return this;
        }

        public CustomerSearchTestHelper setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public CustomerSearchTestHelper setPageSize(String pageSize) {
            this.pageSize = pageSize;
            return this;
        }
    }
}
