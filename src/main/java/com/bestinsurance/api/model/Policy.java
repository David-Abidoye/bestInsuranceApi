package com.bestinsurance.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "POLICIES")
@EntityListeners(AuditingEntityListener.class)
public class Policy implements DomainObject<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "POLICY_ID", updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Column(nullable = false, unique = true, length = 16)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @NotNull
    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal price;

    @CreatedDate
    @Column(name = "CREATED", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "POLICY_COVERAGES",
            joinColumns = @JoinColumn(name = "POLICY_ID"),
            inverseJoinColumns = @JoinColumn(name = "COVERAGE_ID"))
    private Set<Coverage> coverages;

    @OneToMany(mappedBy = "policy")
    private Set<Subscription> policySubscriptions;
}
