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
@Table(name = "CITIES")
public class City {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "CITY_ID")
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String name;

    private int population;

    @ManyToOne
    @JoinColumn(name = "STATE_ID")
    private State state;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", nullable = false)
    private Country country;
}
