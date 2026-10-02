package com.minesafe.minesafe_backend.repository;

import com.minesafe.minesafe_backend.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}