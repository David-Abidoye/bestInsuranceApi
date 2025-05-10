package com.bestinsurance.api.model.events;

import com.bestinsurance.api.model.Subscription;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubscriptionUpdatedEvent {

    private final Subscription subscription;
}
