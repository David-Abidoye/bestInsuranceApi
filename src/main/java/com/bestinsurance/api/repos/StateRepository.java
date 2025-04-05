package com.bestinsurance.api.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.State;

public interface StateRepository extends JpaRepository<State, UUID> {
}
