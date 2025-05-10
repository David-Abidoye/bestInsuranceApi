package com.bestinsurance.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.bestinsurance.api.dto.SubscriptionLogMsg;
import com.bestinsurance.api.mapper.SubscriptionResponseMapper;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.events.SubscriptionUpdatedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SubscriptionEventListener {

    private final SubscriptionResponseMapper subscriptionResponseMapper;
    private final JmsTemplate jmsTemplate;

    @Value("${eventlistener.queue.name}")
    private String queueName;
    @Value("${eventlistener.enabled}")
    private boolean enabled;

    public SubscriptionEventListener(SubscriptionResponseMapper subscriptionResponseMapper, JmsTemplate jmsTemplate) {
        this.subscriptionResponseMapper = subscriptionResponseMapper;
        this.jmsTemplate = jmsTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void subscriptionUpdatedEventHandler(SubscriptionUpdatedEvent event) {
        if (enabled) {
            try {
                Subscription subscription = event.getSubscription();
                SubscriptionLogMsg subscriptionMessage = subscriptionResponseMapper.mapSubscriptionToMsg(subscription);
                jmsTemplate.convertAndSend(queueName, subscriptionMessage);
            } catch (Exception e) {
                log.error("Error during notification process of subscription write operation with exception", e);
            }
        }
    }
}
