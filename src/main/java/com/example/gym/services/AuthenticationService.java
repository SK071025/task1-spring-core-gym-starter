package com.example.gym.services;

import com.example.gym.daos.TraineeDao;
import com.example.gym.daos.TrainerDao;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService
{
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    public User authenticate(String username, String password) {
        logger.debug("Attempting authentication for username: {}", username);

        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent() && password.equals(trainee.get().getPassword())) {
            logger.info("Successful authentication for trainee: {}", username);
            return trainee.get();
        }

        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if (trainer.isPresent() && password.equals(trainer.get().getPassword())) {
            logger.info("Successful authentication for trainer: {}", username);
            return trainer.get();
        }

        logger.warn("Failed authentication attempt for username: {}", username);
        return null;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
}
