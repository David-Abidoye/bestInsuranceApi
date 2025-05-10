package com.bestinsurance.api.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import com.bestinsurance.api.dto.SubscriptionLogMsg;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MessagingConfig {

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setTypeIdPropertyName("_typeId");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("subscriptionMsg", SubscriptionLogMsg.class);
        messageConverter.setTypeIdMappings(typeIdMappings);
        return messageConverter;
    }
}
