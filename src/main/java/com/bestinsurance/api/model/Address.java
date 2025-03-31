package com.bestinsurance.api.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "ADDRESSES")
public class Address {

    @Id
    @Column(name = "ADDRESS_ID")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Column(name = "ADDRESS", nullable = false)
    private String addressLine;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "STATE_ID", nullable = false)
    private State state;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", nullable = false)
    private Country country;
}
