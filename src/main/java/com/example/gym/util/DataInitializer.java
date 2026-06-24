package com.example.gym.util;

import com.example.gym.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    private Map<Long, Training> trainingStorage;

    @Value("${data.trainee.file.path}")
    private String traineeFilePath;

    @Value("${data.trainer.file.path}")
    private String trainerFilePath;

    @Value("${data.training.file.path}")
    private String trainingFilePath;

    @PostConstruct
    public void initializeData() {
        logger.info("Initializing storage with data from files");
        loadTrainees();
        loadTrainers();
        loadTrainings();
        logger.info("Data initialization completed. Loaded {} trainees, {} trainers, {} trainings",
                traineeStorage.size(), trainerStorage.size(), trainingStorage.size());
    }

    private void loadTrainees() {
        try (BufferedReader reader = new BufferedReader(new FileReader(traineeFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Trainee trainee = parseTrainee(line);
                    if (trainee != null) {
                        traineeStorage.put(trainee.getId(), trainee);
                        logger.debug("Loaded trainee: {} {}", trainee.getFirstName(), trainee.getLastName());
                    }
                } catch (Exception e) {
                    logger.error("Error parsing trainee line: {}", line, e);
                }
            }
        } catch (IOException e) {
            logger.error("Error loading trainees from file: {}", traineeFilePath, e);
        }
    }

    private void loadTrainers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(trainerFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Trainer trainer = parseTrainer(line);
                    if (trainer != null) {
                        trainerStorage.put(trainer.getId(), trainer);
                        logger.debug("Loaded trainer: {} {}", trainer.getFirstName(), trainer.getLastName());
                    }
                } catch (Exception e) {
                    logger.error("Error parsing trainer line: {}", line, e);
                }
            }
        } catch (IOException e) {
            logger.error("Error loading trainers from file: {}", trainerFilePath, e);
        }
    }

    private void loadTrainings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(trainingFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Training training = parseTraining(line);
                    if (training != null) {
                        trainingStorage.put(training.getId(), training);
                        logger.debug("Loaded training: {}", training.getTrainingName());
                    }
                } catch (Exception e) {
                    logger.error("Error parsing training line: {}", line, e);
                }
            }
        } catch (IOException e) {
            logger.error("Error loading trainings from file: {}", trainingFilePath, e);
        }
    }

    private Trainee parseTrainee(String csvLine) {
        String[] fields = parseCsvLine(csvLine);

        if (fields.length < 7) {
            logger.warn("Invalid trainee CSV line (expected 7 fields): {}", csvLine);
            return null;
        }

        try {
            String firstName = fields[0].trim();
            String lastName = fields[1].trim();
            String username = fields[2].trim();
            String password = fields[3].trim();
            boolean isActive = Boolean.parseBoolean(fields[4].trim());
            LocalDate dateOfBirth = parseDate(fields[5].trim());
            String address = fields[6].trim();

            return Trainee.builder()
                    .id(IdGenerator.nextTraineeId())
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(username)
                    .password(password)
                    .isActive(isActive)
                    .dateOfBirth(dateOfBirth)
                    .address(address)
                    .build();

        } catch (Exception e) {
            logger.error("Error parsing trainee fields from line: {}", csvLine, e);
            return null;
        }
    }

    private Trainer parseTrainer(String csvLine) {
        String[] fields = parseCsvLine(csvLine);

        if (fields.length < 6) {
            logger.warn("Invalid trainer CSV line (expected 6 fields): {}", csvLine);
            return null;
        }

        try {
            String firstName = fields[0].trim();
            String lastName = fields[1].trim();
            String username = fields[2].trim();
            String password = fields[3].trim();
            boolean isActive = Boolean.parseBoolean(fields[4].trim());
            String specialization = fields[5].trim();

            return Trainer.builder()
                    .id(IdGenerator.nextTrainerId())
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(username)
                    .password(password)
                    .isActive(isActive)
                    .specialization(specialization)
                    .build();

        } catch (Exception e) {
            logger.error("Error parsing trainer fields from line: {}", csvLine, e);
            return null;
        }
    }

    private Training parseTraining(String csvLine) {
        String[] fields = parseCsvLine(csvLine);

        if (fields.length < 6) {
            logger.warn("Invalid training CSV line (expected 6 fields): {}", csvLine);
            return null;
        }

        try {
            Long traineeId = Long.parseLong(fields[0].trim());
            Long trainerId = Long.parseLong(fields[1].trim());
            String trainingName = fields[2].trim();
            TrainingType trainingType = parseTrainingType(fields[3].trim());
            LocalDateTime trainingDate = parseDateTime(fields[4].trim());
            Duration trainingDuration = Duration.ofMinutes(Long.parseLong(fields[5].trim()));

            return Training.builder()
                    .id(IdGenerator.nextTrainingId())
                    .traineeId(traineeId)
                    .trainerId(trainerId)
                    .trainingName(trainingName)
                    .trainingType(trainingType)
                    .trainingDate(trainingDate)
                    .trainingDuration(trainingDuration)
                    .build();

        } catch (Exception e) {
            logger.error("Error parsing training fields from line: {}", csvLine, e);
            return null;
        }
    }

    private String[] parseCsvLine(String csvLine) {
        return csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date: {}", dateStr, e);
            return null;
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTimeStr.trim(), DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing datetime: {}", dateTimeStr, e);
            return null;
        }
    }

    private TrainingType parseTrainingType(String typeStr) {
        if (typeStr == null || typeStr.trim().isEmpty()) {
            return null;
        }

        try {
            return TrainingType.valueOf(typeStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Unknown training type: {}", typeStr, e);
            return TrainingType.CARDIO; // Default fallback
        }
    }
}