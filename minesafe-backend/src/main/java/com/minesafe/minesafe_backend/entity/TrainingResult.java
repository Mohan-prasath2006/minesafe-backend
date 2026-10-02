package com.minesafe.minesafe_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_results")
public class TrainingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long scenarioId;

    private double reactionTime;

    private int score;

    private boolean completed;

    public TrainingResult() {
    }

    public TrainingResult(Long userId, Long scenarioId,
                          double reactionTime, int score,
                          boolean completed) {
        this.userId = userId;
        this.scenarioId = scenarioId;
        this.reactionTime = reactionTime;
        this.score = score;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public double getReactionTime() {
        return reactionTime;
    }

    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}