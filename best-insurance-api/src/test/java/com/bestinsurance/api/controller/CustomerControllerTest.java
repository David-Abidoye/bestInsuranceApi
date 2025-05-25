package com.bestinsurance.api.controller;

import static com.bestinsurance.api.security.Roles.ADMIN;
import static com.bestinsurance.api.security.Roles.CUSTOMER;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import com.bestinsurance.api.dto.AddressView;
import com.bestinsurance.api.dto.CustomerCreateRequest;
import com.bestinsurance.api.dto.CustomerUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper om = new ObjectMapper();

    @BeforeAll
    void init() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateCustomer_whenValidRequest() throws Exception {
        CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                .name("testName")
                .surname("testSurname")
                .birthDate(LocalDate.of(1990, Month.JANUARY, 1))
                .email("testEmail@email.com")
                .street("123 Test Street, APT 4")
                .postalCode("12345")
                .idCity("45576d7c-8d84-4422-9440-19ef80fa16f3")
                .idCountry("d4153ed2-91e6-40da-a3c5-1de8a6d0119c")
                .idState("4b62177f-7eb0-448e-86c1-3e168e44cc29")
                .build();

        mockMvc.perform(post("/customers")
                        .with(user("customer").roles(CUSTOMER))
                        .content(om.writeValueAsString(customerCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpectAll(getAddressExpectations())
                .andReturn();
    }

    @Test
    void shouldGetCustomerById_whenValidId() throws Exception {
        String id = JsonPath.parse(saveACustomer()).read("$.id");
        mockMvc.perform(get("/customers/{id}", id)
                        .with(user("customer").roles(CUSTOMER))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.surname", is("testSurname")))
                .andExpect(jsonPath("$.email", is("testEmail@email.com")))
                .andExpectAll(getAddressExpectations())
                .andReturn();
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        saveACustomer();
        mockMvc.perform(get("/customers")
                        .with(user("admin").roles(ADMIN))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
    }

    @Test
    void shouldUpdateACustomer_whenValidRequest() throws Exception {
        String id = JsonPath.parse(saveACustomer()).read("$.id");
        CustomerUpdateRequest customerUpdateRequest = CustomerUpdateRequest.builder()
                .email("updatedEmail@email.com")
                .street("456 Updated Street")
                .postalCode("54321")
                .idCity("45576d7c-8d84-4422-9440-19ef80fa16f3")
                .idCountry("d4153ed2-91e6-40da-a3c5-1de8a6d0119c")
                .idState("4b62177f-7eb0-448e-86c1-3e168e44cc29")
                .build();

        mockMvc.perform(put("/customers/{id}", id)
                        .with(user("customer").roles(CUSTOMER))
                        .content(om.writeValueAsString(customerUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.surname", is("testSurname")))
                .andExpect(jsonPath("$.birthDate", is("1990-01-01")))
                .andExpect(jsonPath("$.email", is("updatedEmail@email.com")))
                .andExpectAll(getAddressExpectations())
                .andExpect(jsonPath("$.address.address", is("456 Updated Street")))
                .andExpect(jsonPath("$.address.postalCode", is("54321")))
                .andReturn();
    }

    @Test
    void shouldDeleteACustomer() throws Exception {
        String id = JsonPath.parse(saveACustomer()).read("$.id");

        mockMvc.perform(delete("/customers/{id}", id)
                        .with(user("admin").roles(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerById_shouldReturnNotFound_whenIdNotExists() throws Exception {
        mockMvc.perform(get("/customers/4e778109-e65d-4907-bf03-d2fc989ea34c")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("customer").roles(CUSTOMER)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Resource not found: Customer with id: 4e778109-e65d-4907-bf03-d2fc989ea34c does not exist!")))
                .andReturn();
    }

    @Test
    void createCustomerShouldReturnConflict_whenEmailAlreadyExist() throws Exception {
        saveACustomer();
        CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                .name("testName")
                .surname("testSurname")
                .birthDate(LocalDate.of(1990, Month.JANUARY, 1))
                .email("testEmail@email.com")
                .street("123 Test Street, APT 4")
                .postalCode("12345")
                .idCity("45576d7c-8d84-4422-9440-19ef80fa16f3")
                .idCountry("d4153ed2-91e6-40da-a3c5-1de8a6d0119c")
                .idState("4b62177f-7eb0-448e-86c1-3e168e44cc29")
                .build();

        mockMvc.perform(post("/customers")
                        .content(om.writeValueAsString(customerCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("customer").roles(CUSTOMER)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Customer with email testEmail@email.com already exists")))
                .andReturn();
    }

    private String saveACustomer() throws Exception {
        CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                .name("testName")
                .surname("testSurname")
                .birthDate(LocalDate.of(1990, Month.JANUARY, 1))
                .email("testEmail@email.com")
                .street("123 Test Street, APT 4")
                .postalCode("12345")
                .idCity("45576d7c-8d84-4422-9440-19ef80fa16f3")
                .idCountry("d4153ed2-91e6-40da-a3c5-1de8a6d0119c")
                .idState("4b62177f-7eb0-448e-86c1-3e168e44cc29")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post("/customers")
                        .content(om.writeValueAsString(customerCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("customer").roles(CUSTOMER)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpectAll(getAddressExpectations())
                .andReturn().getResponse();
        return response.getContentAsString();
    }

    private ResultMatcher[] getAddressExpectations() {
        return new ResultMatcher[] {
                jsonPath("$.address", notNullValue(), AddressView.class),
                jsonPath("$.address.id", notNullValue()),
                jsonPath("$.address.city.name", notNullValue()),
                jsonPath("$.address.country.name", notNullValue()),
                jsonPath("$.address.state.name", notNullValue())
        };
    }
}
