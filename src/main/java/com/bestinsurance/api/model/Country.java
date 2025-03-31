package com.bestinsurance.api.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "COUNTRIES")
public class Country {

    @Id
    @Column(name = "COUNTRY_ID")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String name;

    private int population;
}
