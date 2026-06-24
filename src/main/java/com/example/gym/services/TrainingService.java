package com.example.gym.services;

import com.example.gym.daos.TrainingDao;
import com.example.gym.models.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;

    public Training createTraining(Training training) {
        logger.info("Creating new training: {}", training.getTrainingName());
        trainingDao.save(training);
        return training;
    }

    public Training getTraining(Long id) {
        logger.debug("Getting training with ID: {}", id);
        return trainingDao.findById(id);
    }

    public List<Training> getAllTrainings() {
        logger.debug("Getting all trainings");
        return trainingDao.findAll();
    }

    public List<Training> getTrainingsByTrainee(Long traineeId) {
        logger.debug("Getting trainings for trainee ID: {}", traineeId);
        return trainingDao.findByTraineeId(traineeId);
    }

    public List<Training> getTrainingsByTrainer(Long trainerId) {
        logger.debug("Getting trainings for trainer ID: {}", trainerId);
        return trainingDao.findByTrainerId(trainerId);
    }

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }
}
