package com.bestinsurance.api.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}
