package com.bestinsurance.api.controller;

import static com.bestinsurance.api.security.Roles.ADMIN;
import static com.bestinsurance.api.security.Roles.CUSTOMER;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import com.bestinsurance.api.common.AbstractPolicyInitializedTest;
import com.bestinsurance.api.dto.PolicyResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PolicyControllerTest extends AbstractPolicyInitializedTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper om = new ObjectMapper();

    @BeforeAll
    void setUpOm() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    void testAllFiltersInitialized() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters("150", "300", "1000", "A", "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters("150", "300", "1000", "A", null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters("150", "300", "1000", "A", "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceMoreThanNameContainingSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters("150", null, null, "A", "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters("150", null, null, "A", null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters("150", null, null, "A", "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceLessThanNameContainingSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, "200", null, "A", "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters(null, "200", null, "A", null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, "200", null, "A", "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 3);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceMoreThanSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters("150", null, null, null, "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 20);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters("150", null, null, null, null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 20);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters("150", null, null, null, "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 20);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceLessThanSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, "200", null, null, "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters(null, "200", null, null, null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, "200", null, null, "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceNameSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, null, "100", "A", "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters(null, null, "100", "A", null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, null, "100", "A", "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 2);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testBetweenPrices() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters("150", "300", null, null, "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters("150", "300", null, null, null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters("150", "300", null, null, "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 30);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testNameSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, null, null, "Double", "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByPrice(policies));

        queryParameters = mapOfQueryParameters(null, null, null, "Double", null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, null, null, "Double", "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceSearch() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, null, "200", null, "PRICE");
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, null, "200", null, null);
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByName(policies));

        queryParameters = mapOfQueryParameters(null, null, "200", null, "NAME");
        policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 10);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testNoFilters() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters(null, null, null, null, null);
        List<PolicyResponse> policies = assertPoliciesRequestWithFiltersReturnsExpectedSize(queryParameters, 50);
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void shouldThrowExceptionForInvalidQueryParameter() throws Exception {
        MultiValueMap<String, String> queryParameters = mapOfQueryParameters("a", null, null, null, null);
        assertInvalidFiltersThrowsExceptionWithMessage(queryParameters, "Invalid input: Price entry [a] is invalid");

        queryParameters = mapOfQueryParameters(null, "10000", null, null, null);
        assertInvalidFiltersThrowsExceptionWithMessage(queryParameters, "Invalid input: Price entry [10000] is out of bounds");

        queryParameters = mapOfQueryParameters(null, null, null, null, "name");
        assertInvalidFiltersThrowsExceptionWithMessage(queryParameters, "Invalid input: Invalid parameter to order policies by: name");

        queryParameters = mapOfQueryParameters(null, null, null, null, "xyz");
        assertInvalidFiltersThrowsExceptionWithMessage(queryParameters, "Invalid input: Invalid parameter to order policies by: xyz");
    }

    private List<PolicyResponse> assertPoliciesRequestWithFiltersReturnsExpectedSize(MultiValueMap<String, String> queryParameters, int size) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/policies")
                        .with(user("admin").roles(ADMIN))
                        .queryParams(queryParameters)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)))
                .andReturn().getResponse();
        return om.readValue(response.getContentAsString(), new TypeReference<>() {});
    }

    private void assertInvalidFiltersThrowsExceptionWithMessage(MultiValueMap<String, String> queryParameters, String message) throws Exception {
        mockMvc.perform(get("/policies")
                        .with(user("admin").roles(ADMIN))
                        .queryParams(queryParameters)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andReturn();
    }

    private MultiValueMap<String, String> mapOfQueryParameters(String priceMoreThan, String priceLessThan,
                                                               String price, String nameContains, String orderBy) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("priceMoreThan", priceMoreThan);
        queryParameters.put("priceLessThan", priceLessThan);
        queryParameters.put("price", price);
        queryParameters.put("nameContains", nameContains);
        queryParameters.put("orderBy", orderBy);
        return MultiValueMap.fromSingleValue(queryParameters);
    }

    private boolean isOrderedByPrice(List<PolicyResponse> policies) {
        BigDecimal initV = new BigDecimal(0);
        for (PolicyResponse p : policies) {
            if (p.getPrice().compareTo(initV) == -1) {
                return false;
            }
            initV = p.getPrice();
        }
        return true;
    }

    private boolean isOrderedByName(List<PolicyResponse> policies) {
        for (int i = 1; i < policies.size(); i++) {
            if (policies.get(i).getName().compareTo(policies.get(i - 1).getName()) == -1) {
                return false;
            }
        }
        return true;
    }
}
