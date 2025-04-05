package com.bestinsurance.api.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.City;

public interface CityRepository extends JpaRepository<City, UUID> {
    // Custom query methods can be defined here if needed
}
