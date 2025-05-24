package com.bestinsurance.api.repos;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.bestinsurance.api.model.State;

public interface StateRepository extends JpaRepository<State, UUID> {

    @Query("SELECT DISTINCT state FROM State state JOIN FETCH state.stateCities city ORDER BY state.name, city.name ASC")
    List<State> selectStatesAndCitiesOrdered();
}
