package com.minesafe.minesafe_backend.controller;

import com.minesafe.minesafe_backend.entity.TrainingResult;
import com.minesafe.minesafe_backend.repository.TrainingResultRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@CrossOrigin
public class TrainingController {

    private final TrainingResultRepository trainingResultRepository;

    public TrainingController(
            TrainingResultRepository trainingResultRepository) {
        this.trainingResultRepository = trainingResultRepository;
    }

    @PostMapping("/complete")
    public TrainingResult completeTraining(
            @RequestBody TrainingResult result) {

        result.setCompleted(true);

        return trainingResultRepository.save(result);
    }

    @GetMapping("/history/{userId}")
    public List<TrainingResult> getTrainingHistory(
            @PathVariable Long userId) {

        return trainingResultRepository.findByUserId(userId);
    }

    @GetMapping("/scenario/{scenarioId}")
    public List<TrainingResult> getScenarioResults(
            @PathVariable Long scenarioId) {

        return trainingResultRepository
                .findByScenarioId(scenarioId);
    }
    @GetMapping("/score/{userId}")
    public int getTotalScore(@PathVariable Long userId) {

        return trainingResultRepository
                .findByUserId(userId)
                .stream()
                .mapToInt(TrainingResult::getScore)
                .sum();
    }
}