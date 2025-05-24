package com.bestinsurance.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.bestinsurance.api.common.AbstractCustomerSubscriptionStatesTest;
import com.bestinsurance.api.dto.SubscriptionRevenueResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SubscriptionQueryTest extends AbstractCustomerSubscriptionStatesTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void init() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    void testRevenues() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/subscriptions/revenues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(51)))
                .andReturn();
        List<SubscriptionRevenueResponse> list = om.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        for (SubscriptionRevenueResponse revenue: list) {
            if (revenue.getStateName().equals("Rhode Island")) {
                assertEquals(new BigDecimal("800.00"), revenue.getRevenue());
                assertEquals(8L, revenue.getCustomersCount());
            } else if (revenue.getStateName().equals("District of Columbia") || revenue.getStateName().equals("Hawaii")) {
                assertEquals(new BigDecimal("100.00"), revenue.getRevenue());
                assertEquals(1L, revenue.getCustomersCount());
            }else {
                assertEquals(new BigDecimal("1000.00"), revenue.getRevenue());
                assertEquals(10L, revenue.getCustomersCount());
            }
        }
    }
}
