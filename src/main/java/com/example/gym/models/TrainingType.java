package com.example.gym.models;

public enum TrainingType {
    YOGA("Yoga"),
    CARDIO("Cardio"),
    STRENGTH("Strength Training"),
    PILATES("Pilates"),
    CROSSFIT("CrossFit"),
    BOXING("Boxing"),
    SWIMMING("Swimming"),
    RUNNING("Running");

    private final String displayName;

    TrainingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}