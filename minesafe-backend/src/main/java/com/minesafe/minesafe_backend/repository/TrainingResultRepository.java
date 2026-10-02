package com.minesafe.minesafe_backend.repository;

import com.minesafe.minesafe_backend.entity.TrainingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingResultRepository
        extends JpaRepository<TrainingResult, Long> {

    List<TrainingResult> findByUserId(Long userId);

    List<TrainingResult> findByScenarioId(Long scenarioId);
}