package com.example.gym.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    public Training() {}

    public Training(Long id, Long traineeId, Long trainerId, String trainingName,
                    TrainingType trainingType, LocalDateTime trainingDate, Duration trainingDuration) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Long getId() { return id; }
    public Long getTraineeId() { return traineeId; }
    public Long getTrainerId() { return trainerId; }
    public String getTrainingName() { return trainingName; }
    public TrainingType getTrainingType() { return trainingType; }
    public LocalDateTime getTrainingDate() { return trainingDate; }
    public Duration getTrainingDuration() { return trainingDuration; }

    public void setId(Long id) { this.id = id; }
    public void setTraineeId(Long traineeId) { this.traineeId = traineeId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
    public void setTrainingName(String trainingName) { this.trainingName = trainingName; }
    public void setTrainingType(TrainingType trainingType) { this.trainingType = trainingType; }
    public void setTrainingDate(LocalDateTime trainingDate) { this.trainingDate = trainingDate; }
    public void setTrainingDuration(Duration trainingDuration) { this.trainingDuration = trainingDuration; }

    public static class Builder {
        private Long id;
        private Long traineeId;
        private Long trainerId;
        private String trainingName;
        private TrainingType trainingType;
        private LocalDateTime trainingDate;
        private Duration trainingDuration;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder traineeId(Long traineeId) {
            this.traineeId = traineeId;
            return this;
        }

        public Builder trainerId(Long trainerId) {
            this.trainerId = trainerId;
            return this;
        }

        public Builder trainingName(String trainingName) {
            this.trainingName = trainingName;
            return this;
        }

        public Builder trainingType(TrainingType trainingType) {
            this.trainingType = trainingType;
            return this;
        }

        public Builder trainingDate(LocalDateTime trainingDate) {
            this.trainingDate = trainingDate;
            return this;
        }

        public Builder trainingDuration(Duration trainingDuration) {
            this.trainingDuration = trainingDuration;
            return this;
        }

        public Training build() {
            return new Training(id, traineeId, trainerId, trainingName,
                    trainingType, trainingDate, trainingDuration);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}