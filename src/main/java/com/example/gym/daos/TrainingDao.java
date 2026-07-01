package com.example.gym.daos;

import com.example.gym.dtos.TraineeTrainingSearchCriteria;
import com.example.gym.dtos.TrainerTrainingSearchCriteria;
import com.example.gym.entities.TrainingEntity;
import java.util.List;

public interface TrainingDao extends BaseDao<TrainingEntity> {
    List<TrainingEntity> findByTraineeId(Long traineeId);

    List<TrainingEntity> findByTrainerId(Long trainerId);

    List<TrainingEntity> findByTrainingType(Long trainingTypeId);

    List<TrainingEntity> findTraineeTrainingsByCriteria(
            String traineeUsername,
            TraineeTrainingSearchCriteria criteria);

    List<TrainingEntity> findTrainerTrainingsByCriteria(
            String trainerUsername,
            TrainerTrainingSearchCriteria criteria);
}