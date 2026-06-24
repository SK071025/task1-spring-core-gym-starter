package com.example.gym.daos;

import com.example.gym.models.Training;
import com.example.gym.models.TrainingType;

import java.util.List;

public interface TrainingDao extends BaseDao<Training> {
    List<Training> findByTraineeId(Long traineeId);
    List<Training> findByTrainerId(Long trainerId);
    List<Training> findByTrainingType(TrainingType trainingType);
}