package com.bestinsurance.api.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "CUSTOMERS")
public class Customer {

    @Id
    @Column(name = "CUSTOMER_ID")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String surname;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(name = "CREATED", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "UPDATED", nullable = false)
    private OffsetDateTime updatedAt;

    @NotNull
    @OneToOne
    @JoinColumn(name = "ADDRESS")
    private Address address;
}
