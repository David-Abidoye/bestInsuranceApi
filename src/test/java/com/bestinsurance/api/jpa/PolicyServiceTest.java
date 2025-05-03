package com.bestinsurance.api.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.bestinsurance.api.common.AbstractPolicyInitializedTest;
import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.service.PolicyService;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Import({DomainConfig.class, PolicyService.class})
class PolicyServiceTest extends AbstractPolicyInitializedTest {

    @Autowired
    private PolicyService policyService;

    @Test
    void testAllFiltersInitialized() {
        List<Policy> policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                new BigDecimal(1000),"A", PolicyService.PolicyOrderBy.valueOf("PRICE"));

        assertEquals(3, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                new BigDecimal(1000),"A", null);

        assertEquals(3, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                new BigDecimal(1000),"A", PolicyService.PolicyOrderBy.valueOf("NAME"));

        assertEquals(3, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceMoreThanNameContainingSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(new BigDecimal(150),null,
                null,"A", PolicyService.PolicyOrderBy.valueOf("PRICE"));

        assertEquals(2, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), null,
                null,"A", null);

        assertEquals(2, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), null,
                null,"A", PolicyService.PolicyOrderBy.valueOf("NAME"));

        assertEquals(2, policies.size());
        assertTrue(isOrderedByName(policies));
    }
    @Test
    void testPriceLessThanNameContainingSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null,"A", PolicyService.PolicyOrderBy.valueOf("PRICE"));

        assertEquals(3, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null,"A", null);

        assertEquals(3, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null,"A", PolicyService.PolicyOrderBy.valueOf("NAME"));

        assertEquals(3, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceMoreThanSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(new BigDecimal(150),null,
                null,null, PolicyService.PolicyOrderBy.valueOf("PRICE"));

        assertEquals(20, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), null,
                null,null, null);

        assertEquals(20, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(new BigDecimal(150), null,
                null,null, PolicyService.PolicyOrderBy.valueOf("NAME"));

        assertEquals(20, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceLessThanSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null,null, PolicyService.PolicyOrderBy.valueOf("PRICE"));

        assertEquals(30, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null, null, null);

        assertEquals(30, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, new BigDecimal(200),
                null,null, PolicyService.PolicyOrderBy.valueOf("NAME"));

        assertEquals(30, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testPriceNameSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(100),"A", PolicyService.PolicyOrderBy.valueOf("PRICE"));
        assertEquals(2, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(100),"A", null);
        assertEquals(2, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(100),"A", PolicyService.PolicyOrderBy.valueOf("NAME"));
        assertEquals(2, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testBetweenPrices() {
        List<Policy> policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                null,null, PolicyService.PolicyOrderBy.valueOf("PRICE"));
        assertEquals(30, policies.size());
        assertTrue(isOrderedByPrice(policies));
        policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                null,null, PolicyService.PolicyOrderBy.valueOf("NAME"));
        assertEquals(30, policies.size());
        assertTrue(isOrderedByName(policies));
        policies = this.policyService.findAllWithFilters(new BigDecimal(150), new BigDecimal(300),
                null,null, null);
        assertEquals(30, policies.size());
        assertTrue(isOrderedByName(policies));
    }

    @Test
    void testNameSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(null, null,
                null,"Double", PolicyService.PolicyOrderBy.valueOf("PRICE"));
        assertEquals(10, policies.size());
        assertTrue(isOrderedByPrice(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                null,"Double", null);
        assertEquals(10, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                null,"Double", PolicyService.PolicyOrderBy.valueOf("NAME"));
        assertEquals(10, policies.size());
        assertTrue(isOrderedByName(policies));

    }

    @Test
    void testPriceSearch() {
        List<Policy> policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(200), null, null);
        assertEquals(10, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(200), null, PolicyService.PolicyOrderBy.valueOf("PRICE"));
        assertEquals(10, policies.size());
        assertTrue(isOrderedByName(policies));

        policies = this.policyService.findAllWithFilters(null, null,
                new BigDecimal(200), null, PolicyService.PolicyOrderBy.valueOf("NAME"));
        assertEquals(10, policies.size());
        assertTrue(isOrderedByName(policies));
    }


    private boolean isOrderedByPrice(List<Policy> policies) {
        BigDecimal initV = new BigDecimal(0);
        for (Policy p: policies) {
            if (p.getPrice().compareTo(initV) == -1) {
                return false;
            }
            initV = p.getPrice();
        }
        return true;
    }

    private boolean isOrderedByName(List<Policy> policies) {
        for (int i = 1; i < policies.size(); i++) {
            if (policies.get(i).getName().compareTo(policies.get(i - 1).getName()) == -1) {
                return false;
            }
        }
        return true;
    }
}