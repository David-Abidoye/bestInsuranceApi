package com.bestinsurance.api.config;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.auditing.DateTimeProvider;
import com.bestinsurance.api.service.SampleDataLoader;

@Configuration
@org.springframework.data.jpa.repository.config.EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class DomainConfig {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

    @Bean
    @DependsOn("liquibase")
    @ConditionalOnProperty(prefix = "dataloader", name = "loadsample", havingValue = "true")
    public SampleDataLoader sampleDataLoader() {
        return new SampleDataLoader();
    }
}
