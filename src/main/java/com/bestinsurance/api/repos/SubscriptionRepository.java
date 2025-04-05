package com.bestinsurance.api.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
