package com.example.gym;


import com.example.gym.daos.TrainingDao;
import com.example.gym.models.Training;
import com.example.gym.models.TrainingType;
import com.example.gym.services.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingService Tests")
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingService.setTrainingDao(trainingDao);
    }

    private Training buildTraining(Long id, Long traineeId, Long trainerId, String name) {
        return Training.builder()
                .id(id)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(name)
                .trainingType(TrainingType.YOGA)
                .trainingDate(LocalDateTime.now())
                .trainingDuration(Duration.ofMinutes(60))
                .build();
    }

    @Nested
    @DisplayName("createTraining()")
    class CreateTrainingTests {

        @Test
        @DisplayName("Should save and return training")
        void shouldSaveAndReturnTraining() {
            Training training = buildTraining(null, 1L, 1L, "Morning Yoga");

            Training result = trainingService.createTraining(training);

            verify(trainingDao).save(training);
            assertThat(result).isEqualTo(training);
        }
    }

    @Nested
    @DisplayName("getTraining()")
    class GetTrainingTests {

        @Test
        @DisplayName("Should return training by ID")
        void shouldReturnTrainingById() {
            Training training = buildTraining(1L, 1L, 1L, "Morning Yoga");
            when(trainingDao.findById(1L)).thenReturn(training);

            Training result = trainingService.getTraining(1L);

            assertThat(result).isEqualTo(training);
        }

        @Test
        @DisplayName("Should return null when training not found")
        void shouldReturnNullWhenTrainingNotFound() {
            when(trainingDao.findById(999L)).thenReturn(null);

            Training result = trainingService.getTraining(999L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getAllTrainings()")
    class GetAllTrainingsTests {

        @Test
        @DisplayName("Should return all trainings")
        void shouldReturnAllTrainings() {
            List<Training> trainings = Arrays.asList(
                    buildTraining(1L, 1L, 1L, "Yoga"),
                    buildTraining(2L, 2L, 2L, "Cardio")
            );
            when(trainingDao.findAll()).thenReturn(trainings);

            List<Training> result = trainingService.getAllTrainings();

            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("getTrainingsByTrainee()")
    class GetTrainingsByTraineeTests {

        @Test
        @DisplayName("Should return trainings for given trainee ID")
        void shouldReturnTrainingsForGivenTraineeId() {
            List<Training> trainings = List.of(buildTraining(1L, 10L, 1L, "Yoga"));
            when(trainingDao.findByTraineeId(10L)).thenReturn(trainings);

            List<Training> result = trainingService.getTrainingsByTrainee(10L);

            assertThat(result).hasSize(1);
            verify(trainingDao).findByTraineeId(10L);
        }
    }

    @Nested
    @DisplayName("getTrainingsByTrainer()")
    class GetTrainingsByTrainerTests {

        @Test
        @DisplayName("Should return trainings for given trainer ID")
        void shouldReturnTrainingsForGivenTrainerId() {
            List<Training> trainings = List.of(buildTraining(1L, 1L, 5L, "Cardio"));
            when(trainingDao.findByTrainerId(5L)).thenReturn(trainings);

            List<Training> result = trainingService.getTrainingsByTrainer(5L);

            assertThat(result).hasSize(1);
            verify(trainingDao).findByTrainerId(5L);
        }
    }
}

