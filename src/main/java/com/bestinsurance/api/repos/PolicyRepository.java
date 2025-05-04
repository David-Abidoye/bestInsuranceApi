package com.bestinsurance.api.repos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, UUID> {

    Optional<Policy> findByName(String name);
    List<Policy> findAllByPriceGreaterThan(BigDecimal price, Sort sort);
    List<Policy> findAllByPriceLessThan(BigDecimal price, Sort sort);
    List<Policy> findAllByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Sort sort);
    List<Policy> findAllByPrice(BigDecimal price, Sort sort);
    List<Policy> findAllByNameContainingIgnoreCase(String nameContains, Sort sort);
    List<Policy> findAllByNameContainingIgnoreCaseAndPrice(String nameContains, BigDecimal price, Sort sort);
    List<Policy> findAllByNameContainingIgnoreCaseAndPriceGreaterThan(String nameContains, BigDecimal price, Sort sort);
    List<Policy> findAllByNameContainingIgnoreCaseAndPriceLessThan(String nameContains, BigDecimal price, Sort sort);
    List<Policy> findAllByNameContainingIgnoreCaseAndPriceBetween(String nameContains, BigDecimal minPrice, BigDecimal maxPrice, Sort sort);
}
