package com.example.gym;

import com.example.gym.daos.TrainingDaoImpl;
import com.example.gym.models.Training;
import com.example.gym.models.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingDaoImpl Tests")
class TrainingDaoImplTest {

    private TrainingDaoImpl trainingDao;
    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void setUp() {
        trainingDao = new TrainingDaoImpl();
        trainingStorage = new HashMap<>();
        ReflectionTestUtils.setField(trainingDao, "trainingStorage", trainingStorage);
    }

    private Training buildTraining(Long id, Long traineeId, Long trainerId,
                                   String name, TrainingType type) {
        return Training.builder()
                .id(id)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(name)
                .trainingType(type)
                .trainingDate(LocalDateTime.now())
                .trainingDuration(Duration.ofMinutes(60))
                .build();
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Should save training with existing ID")
        void shouldSaveTrainingWithExistingId() {
            Training training = buildTraining(1L, 1L, 1L, "Morning Yoga", TrainingType.YOGA);

            trainingDao.save(training);

            assertThat(trainingStorage).containsKey(1L);
        }

        @Test
        @DisplayName("Should auto-generate ID when training has no ID")
        void shouldAutoGenerateIdWhenTrainingHasNoId() {
            Training training = buildTraining(null, 1L, 1L, "Evening Cardio", TrainingType.CARDIO);

            trainingDao.save(training);

            assertThat(training.getId()).isNotNull();
            assertThat(trainingStorage).containsKey(training.getId());
        }
    }

    @Nested
    @DisplayName("findByTraineeId()")
    class FindByTraineeIdTests {

        @Test
        @DisplayName("Should return trainings for given trainee ID")
        void shouldReturnTrainingsForGivenTraineeId() {
            trainingStorage.put(1L, buildTraining(1L, 10L, 1L, "Yoga Session", TrainingType.YOGA));
            trainingStorage.put(2L, buildTraining(2L, 10L, 2L, "Cardio Session", TrainingType.CARDIO));
            trainingStorage.put(3L, buildTraining(3L, 20L, 1L, "Strength", TrainingType.STRENGTH));

            List<Training> result = trainingDao.findByTraineeId(10L);

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(t -> t.getTraineeId().equals(10L));
        }

        @Test
        @DisplayName("Should return empty list when no trainings for trainee")
        void shouldReturnEmptyListWhenNoTrainingsForTrainee() {
            List<Training> result = trainingDao.findByTraineeId(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByTrainerId()")
    class FindByTrainerIdTests {

        @Test
        @DisplayName("Should return trainings for given trainer ID")
        void shouldReturnTrainingsForGivenTrainerId() {
            trainingStorage.put(1L, buildTraining(1L, 1L, 5L, "Yoga Session", TrainingType.YOGA));
            trainingStorage.put(2L, buildTraining(2L, 2L, 5L, "Cardio Session", TrainingType.CARDIO));
            trainingStorage.put(3L, buildTraining(3L, 3L, 6L, "Strength", TrainingType.STRENGTH));

            List<Training> result = trainingDao.findByTrainerId(5L);

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(t -> t.getTrainerId().equals(5L));
        }

        @Test
        @DisplayName("Should return empty list when no trainings for trainer")
        void shouldReturnEmptyListWhenNoTrainingsForTrainer() {
            List<Training> result = trainingDao.findByTrainerId(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByTrainingType()")
    class FindByTrainingTypeTests {

        @Test
        @DisplayName("Should return trainings of given type")
        void shouldReturnTrainingsOfGivenType() {
            trainingStorage.put(1L, buildTraining(1L, 1L, 1L, "Yoga A", TrainingType.YOGA));
            trainingStorage.put(2L, buildTraining(2L, 2L, 2L, "Yoga B", TrainingType.YOGA));
            trainingStorage.put(3L, buildTraining(3L, 3L, 3L, "Cardio", TrainingType.CARDIO));

            List<Training> result = trainingDao.findByTrainingType(TrainingType.YOGA);

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(t -> t.getTrainingType() == TrainingType.YOGA);
        }

        @Test
        @DisplayName("Should return empty list when no trainings of given type")
        void shouldReturnEmptyListWhenNoTrainingsOfGivenType() {
            List<Training> result = trainingDao.findByTrainingType(TrainingType.BOXING);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Should return all trainings")
        void shouldReturnAllTrainings() {
            trainingStorage.put(1L, buildTraining(1L, 1L, 1L, "Yoga", TrainingType.YOGA));
            trainingStorage.put(2L, buildTraining(2L, 2L, 2L, "Cardio", TrainingType.CARDIO));

            List<Training> result = trainingDao.findAll();

            assertThat(result).hasSize(2);
        }
    }
}
