package com.example.gym;

import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.models.User;
import com.example.gym.services.AuthenticationService;
import com.example.gym.services.TraineeService;
import com.example.gym.services.TrainerService;
import com.example.gym.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationFacade {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final AuthenticationService authenticationService;

    public ApplicationFacade(TraineeService traineeService,
                             TrainerService trainerService,
                             TrainingService trainingService,
                             AuthenticationService authenticationService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.authenticationService = authenticationService;
    }

    public Trainee registerTrainee(Trainee trainee) {
        logger.info("Registering new trainee through facade");
        return traineeService.createTrainee(trainee);
    }

    public Trainee updateTraineeProfile(Trainee trainee) {
        logger.info("Updating trainee profile through facade");
        return traineeService.updateTrainee(trainee);
    }

    public void deleteTraineeProfile(Long traineeId) {
        logger.info("Deleting trainee profile through facade");
        traineeService.deleteTrainee(traineeId);
    }

    public Trainee getTraineeProfile(Long traineeId) {
        return traineeService.getTrainee(traineeId);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    public Trainer registerTrainer(Trainer trainer) {
        logger.info("Registering new trainer through facade");
        return trainerService.createTrainer(trainer);
    }

    public Trainer updateTrainerProfile(Trainer trainer) {
        logger.info("Updating trainer profile through facade");
        return trainerService.updateTrainer(trainer);
    }

    public Trainer getTrainerProfile(Long trainerId) {
        return trainerService.getTrainer(trainerId);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public Training scheduleTraining(Training training) {
        logger.info("Scheduling new training through facade");
        return trainingService.createTraining(training);
    }

    public Training getTraining(Long trainingId) {
        return trainingService.getTraining(trainingId);
    }

    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    public List<Training> getTraineeTrainings(Long traineeId) {
        return trainingService.getTrainingsByTrainee(traineeId);
    }

    public List<Training> getTrainerTrainings(Long trainerId) {
        return trainingService.getTrainingsByTrainer(trainerId);
    }

    public User login(String username, String password) {
        logger.info("Login attempt through facade for username: {}", username);
        return authenticationService.authenticate(username, password);
    }
}
