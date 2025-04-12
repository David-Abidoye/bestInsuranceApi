package com.bestinsurance.api.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.bestinsurance.api.common.AbstractCustomerInitializedTest;
import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.model.Customer;
import com.bestinsurance.api.service.CustomerService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DomainConfig.class, CustomerService.class})
class CustomerServiceTest extends AbstractCustomerInitializedTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void testUpdate() {
        List<Customer> all = customerService.findAll();
        Customer customer = all.get(0);
        String oldEmail = customer.getEmail();
        Customer customerUpdatedFields = new Customer();
        customerUpdatedFields.setEmail("newEmail@email.com");
        Customer updated = customerService.update(customer.getId().toString(), customerUpdatedFields);
        assertThat(updated.getName(), is(customer.getName()));
        assertThat(updated.getSurname(), is(customer.getSurname()));
        assertThat(updated.getEmail(), is(not(oldEmail)));
        assertThat(updated.getAddress(), is(customer.getAddress()));
    }
}