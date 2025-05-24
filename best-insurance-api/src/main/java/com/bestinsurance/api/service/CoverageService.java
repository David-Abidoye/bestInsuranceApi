package com.bestinsurance.api.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.Coverage;
import com.bestinsurance.api.repos.CoverageRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CoverageService extends AbstractCrudService<Coverage, UUID> {

    private final CoverageRepository coverageRepository;

    public CoverageService(CoverageRepository coverageRepository) {
        this.coverageRepository = coverageRepository;
    }

    @Override
    protected CoverageRepository getRepository() {
        return coverageRepository;
    }

    @Override
    public Coverage update(UUID id, Coverage coverageUpdateRequest) {
        return getRepository().findById(id)
                .map(coverageToUpdate -> {
                    Optional.ofNullable(coverageUpdateRequest.getName()).ifPresent(coverageToUpdate::setName);
                    Optional.ofNullable(coverageUpdateRequest.getDescription()).ifPresent(coverageToUpdate::setDescription);
                    return getRepository().save(coverageToUpdate);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Subscription with id: %s does not exist!", id)));
    }
}
