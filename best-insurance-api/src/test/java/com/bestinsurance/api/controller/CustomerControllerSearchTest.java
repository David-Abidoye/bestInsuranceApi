package com.bestinsurance.api.controller;

import static com.bestinsurance.api.security.Roles.FRONT_OFFICE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.bestinsurance.api.common.AbstractCustomerWithAssociationsTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerSearchTest extends AbstractCustomerWithAssociationsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSelectCustomersByPolicy() throws Exception {
        mockMvc.perform(get("/customers/policy/{id}", policies.get(0).getId().toString())
                        .with(user("front_office_user").roles(FRONT_OFFICE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testSelectCustomersByCoverage() throws Exception {
        mockMvc.perform(get("/customers/coverage/{id}", coverages.get(0).getId().toString())
                        .with(user("front_office_user").roles(FRONT_OFFICE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testSelectCustomersWithSubscriptionBetween() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(1);
        String startDateString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.now().plusMonths(7);
        String endDateString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mockMvc.perform(get("/customers/subscriptions")
                        .with(user("front_office_user").roles(FRONT_OFFICE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("startDate", startDateString)
                        .queryParam("endDate", endDateString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    void testSelectCustomersWithDiscount() throws Exception {
        mockMvc.perform(get("/customers/subscriptions/discountedPrice")
                        .with(user("front_office_user").roles(FRONT_OFFICE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
