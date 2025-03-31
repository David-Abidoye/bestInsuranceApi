package com.bestinsurance.api.config;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

@Configuration
@org.springframework.data.jpa.repository.config.EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class EnableJpaAuditing {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
