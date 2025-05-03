package com.bestinsurance.api.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.bestinsurance.api.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {

    boolean existsByEmail(String email);

    @Query("SELECT c from Customer c JOIN c.customerSubscriptions s WHERE s.id.policyId = :policyId ORDER BY c.name ASC")
    List<Customer> findAllCustomersByPolicyId(UUID policyId);

    @Query("SELECT c from Customer c JOIN c.customerSubscriptions s JOIN s.policy p JOIN p.coverages cov WHERE cov.id = :coverageId ORDER BY c.name ASC")
    List<Customer> findAllCustomersByCoverageId(UUID coverageId);

    @Query("SELECT c FROM Customer c JOIN c.customerSubscriptions s JOIN s.policy p WHERE s.paidPrice < p.price ORDER BY c.name ASC")
    List<Customer> findAllCustomersWithDiscount();

    @Query("SELECT c FROM Customer c JOIN c.customerSubscriptions s WHERE s.startDate > :startDate AND s.endDate < :endDate ORDER BY c.name ASC")
    List<Customer> findAllCustomersWithActiveSubscriptionBetween(LocalDate startDate, LocalDate endDate);
}
