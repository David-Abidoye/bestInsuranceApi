package com.bestinsurance.api.model;

import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "COVERAGES")
public class Coverage implements DomainObject<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "COVERAGE_ID")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Column(nullable = false, length = 16)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "coverages")
    private Set<Policy> policies;
}
