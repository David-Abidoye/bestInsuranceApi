package com.bestinsurance.api.repos;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.City;

public interface CityRepository extends JpaRepository<City, UUID> {

    Optional<City> findByNameAndStateName(String cityName, String stateName);
}
