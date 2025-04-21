package com.bestinsurance.api.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.model.Policy;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import jakarta.persistence.EntityNotFoundException;

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
}
