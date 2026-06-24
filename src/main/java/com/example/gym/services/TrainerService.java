package com.example.gym.services;

import com.example.gym.daos.TrainerDao;
import com.example.gym.models.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private CredentialsService credentialsService;

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating new trainer: {} {}", trainer.getFirstName(), trainer.getLastName());

        String username = generateUniqueUsername(trainer.getFirstName(), trainer.getLastName());
        String password = credentialsService.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);

        trainerDao.save(trainer);
        logger.info("Created trainer with username: {}", username);
        return trainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer with ID: {}", trainer.getId());
        trainerDao.save(trainer);
        return trainer;
    }

    public Trainer getTrainer(Long id) {
        logger.debug("Getting trainer with ID: {}", id);
        return trainerDao.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        logger.debug("Getting all trainers");
        return trainerDao.findAll();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        boolean exists = trainerDao.findByUsername(baseUsername).isPresent();

        if (!exists) {
            return baseUsername;
        }

        int serialNumber = 1;
        String username;
        do {
            username = baseUsername + serialNumber;
            serialNumber++;
        } while (trainerDao.findByUsername(username).isPresent());

        return username;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setAuthenticationService(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }
}
