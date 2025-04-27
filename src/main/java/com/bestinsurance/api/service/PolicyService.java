package com.bestinsurance.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@Service
public class PolicyService extends AbstractCrudService<Policy, UUID> {

    private final PolicyRepository policyRepository;
    private final CoverageRepository coverageRepository;

    public PolicyService(PolicyRepository policyRepository, CoverageRepository coverageRepository) {
        this.policyRepository = policyRepository;
        this.coverageRepository = coverageRepository;
    }

    @Override
    protected PolicyRepository getRepository() {
        return policyRepository;
    }

    @Override
    public Policy create(Policy policy) {
        enrichCoverage(policy);
        return super.create(policy);
    }

    @Override
    public Policy update(UUID id, Policy policyUpdateRequest) {
        return getRepository().findById(id)
                .map(policyToUpdate -> {
                    Optional.ofNullable(policyUpdateRequest.getName()).ifPresent(policyToUpdate::setName);
                    Optional.ofNullable(policyUpdateRequest.getDescription()).ifPresent(policyToUpdate::setDescription);
                    Optional.ofNullable(policyUpdateRequest.getPrice()).ifPresent(policyToUpdate::setPrice);
                    Optional.ofNullable(policyUpdateRequest.getCoverages()).ifPresent(policyToUpdate::setCoverages);
                    return getRepository().save(policyToUpdate);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Policy with id: %s does not exist!", id)));
    }

    private void enrichCoverage(Policy policy) {
        Set<Coverage> enrichedCoverage = policy.getCoverages().stream().map(
                        coverage -> {
                            UUID coverageId = coverage.getId();
                            return coverageRepository.findById(coverageId)
                                    .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot create policy; coverage with id: %s not found", coverageId)));
                        })
                .collect(Collectors.toSet());
        policy.setCoverages(enrichedCoverage);
    }

    public List<Policy> findAllWithFilters(BigDecimal minPrice, BigDecimal maxPrice,
                                           BigDecimal price, String nameContains, PolicyOrderBy orderBy) {

        Sort sortAsc = getSortOrderAsc(orderBy);

        if (minPrice != null && maxPrice != null && nameContains != null) {
            return getRepository().findAllByNameContainingIgnoreCaseAndPriceBetween(nameContains, minPrice, maxPrice, sortAsc);
        }
        if (price != null && nameContains != null) {
            return getRepository().findAllByNameContainingIgnoreCaseAndPrice(nameContains, price, sortAsc);
        }
        if (minPrice != null && nameContains != null) {
            return getRepository().findAllByNameContainingIgnoreCaseAndPriceGreaterThan(nameContains, minPrice, sortAsc);
        }
        if (maxPrice != null && nameContains != null) {
            return getRepository().findAllByNameContainingIgnoreCaseAndPriceLessThan(nameContains, maxPrice, sortAsc);
        }
        if (minPrice != null && maxPrice != null) {
            return getRepository().findAllByPriceBetween(minPrice, maxPrice, sortAsc);
        }
        if (minPrice != null) {
            return getRepository().findAllByPriceGreaterThan(minPrice, sortAsc);
        }
        if (maxPrice != null) {
            return getRepository().findAllByPriceLessThan(maxPrice, sortAsc);
        }
        if (price != null) {
            return getRepository().findAllByPrice(price, sortAsc);
        }
        if (nameContains != null) {
            return getRepository().findAllByNameContainingIgnoreCase(nameContains, sortAsc);
        }
        return getRepository().findAll(sortAsc);
    }

    private Sort getSortOrderAsc(PolicyOrderBy orderBy) {
        return orderBy == null ? Sort.by(Sort.Direction.ASC, PolicyOrderBy.NAME.getValue()) : Sort.by(Sort.Direction.ASC, orderBy.getValue());
    }

    @Getter
    public enum PolicyOrderBy {
        NAME("name"),
        PRICE("price");

        private final String value;

        PolicyOrderBy(String value) {
            this.value = value;
        }
    }
}
