package com.example.gym.services;

import com.example.gym.daos.TraineeDao;
import com.example.gym.models.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;
    private CredentialsService credentialsService;

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating new trainee: {} {}", trainee.getFirstName(), trainee.getLastName());

        String username = generateUniqueUsername(trainee.getFirstName(), trainee.getLastName());
        String password = credentialsService.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);

        traineeDao.save(trainee);
        logger.info("Created trainee with username: {}", username);
        return trainee;
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee with ID: {}", trainee.getId());
        traineeDao.save(trainee);
        return trainee;
    }

    public void deleteTrainee(Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        traineeDao.delete(id);
    }

    public Trainee getTrainee(Long id) {
        logger.debug("Getting trainee with ID: {}", id);
        return traineeDao.findById(id);
    }

    public List<Trainee> getAllTrainees() {
        logger.debug("Getting all trainees");
        return traineeDao.findAll();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        boolean exists = traineeDao.findByUsername(baseUsername).isPresent();

        if (!exists) {
            return baseUsername;
        }

        int serialNumber = 1;
        String username;
        do {
            username = baseUsername + serialNumber;
            serialNumber++;
        } while (traineeDao.findByUsername(username).isPresent());

        return username;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUsernamePasswordGenerator(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }
}

