package com.bestinsurance.api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.bestinsurance.api.model.SubscriptionRevenue;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
import com.bestinsurance.api.repos.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SubscriptionService extends AbstractCrudService<Subscription, SubscriptionId> {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    protected SubscriptionRepository getRepository() {
        return subscriptionRepository;
    }

    @Override
    public Subscription update(SubscriptionId id, Subscription subscriptionUpdateRequest) {
        return subscriptionRepository.findById(id)
                .map(subscriptionToUpdate -> {
                    updateSubscriptionFields(subscriptionToUpdate, subscriptionUpdateRequest);
                    return subscriptionRepository.save(subscriptionToUpdate);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Subscription with id: %s does not exist!", id)));
    }

    private void updateSubscriptionFields(Subscription subscriptionToUpdate, Subscription subscriptionUpdateRequest) {
        Optional.ofNullable(subscriptionUpdateRequest.getStartDate()).ifPresent(subscriptionToUpdate::setStartDate);
        Optional.ofNullable(subscriptionUpdateRequest.getEndDate()).ifPresent(subscriptionToUpdate::setEndDate);
        Optional.ofNullable(subscriptionUpdateRequest.getPaidPrice()).ifPresent(subscriptionToUpdate::setPaidPrice);
    }

    public List<SubscriptionRevenue> findStateSubscriptionRevenue(){
        return getRepository().selectStateSubscriptionRevenue();
    }
}
