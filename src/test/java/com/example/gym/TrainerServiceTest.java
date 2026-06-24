package com.example.gym;

import com.example.gym.daos.TrainerDao;
import com.example.gym.models.Trainer;
import com.example.gym.services.CredentialsService;
import com.example.gym.services.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainerService Tests")
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerService.setTrainerDao(trainerDao);
        trainerService.setAuthenticationService(credentialsService);
    }

    private Trainer buildTrainer(Long id, String firstName, String lastName, String specialization) {
        return Trainer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(true)
                .specialization(specialization)
                .build();
    }

    @Nested
    @DisplayName("createTrainer()")
    class CreateTrainerTests {

        @Test
        @DisplayName("Should create trainer with generated username and password")
        void shouldCreateTrainerWithGeneratedUsernameAndPassword() {
            Trainer trainer = buildTrainer(null, "Mike", "Smith", "YOGA");
            when(trainerDao.findByUsername("Mike.Smith")).thenReturn(Optional.empty());
            when(credentialsService.generatePassword()).thenReturn("trainerPass1");

            Trainer result = trainerService.createTrainer(trainer);

            assertThat(result.getUsername()).isEqualTo("Mike.Smith");
            assertThat(result.getPassword()).isEqualTo("trainerPass1");
            verify(trainerDao).save(trainer);
        }

        @Test
        @DisplayName("Should append serial number when username already exists")
        void shouldAppendSerialNumberWhenUsernameAlreadyExists() {
            Trainer trainer = buildTrainer(null, "Mike", "Smith", "YOGA");
            Trainer existing = buildTrainer(1L, "Mike", "Smith", "CARDIO");
            existing.setUsername("Mike.Smith");

            when(trainerDao.findByUsername("Mike.Smith")).thenReturn(Optional.of(existing));
            when(trainerDao.findByUsername("Mike.Smith1")).thenReturn(Optional.empty());
            when(credentialsService.generatePassword()).thenReturn("pass");

            Trainer result = trainerService.createTrainer(trainer);

            assertThat(result.getUsername()).isEqualTo("Mike.Smith1");
        }
    }

    @Nested
    @DisplayName("updateTrainer()")
    class UpdateTrainerTests {

        @Test
        @DisplayName("Should save and return updated trainer")
        void shouldSaveAndReturnUpdatedTrainer() {
            Trainer trainer = buildTrainer(1L, "Mike", "Smith", "YOGA");

            Trainer result = trainerService.updateTrainer(trainer);

            verify(trainerDao).save(trainer);
            assertThat(result).isEqualTo(trainer);
        }
    }

    @Nested
    @DisplayName("getTrainer()")
    class GetTrainerTests {

        @Test
        @DisplayName("Should return trainer by ID")
        void shouldReturnTrainerById() {
            Trainer trainer = buildTrainer(1L, "Mike", "Smith", "YOGA");
            when(trainerDao.findById(1L)).thenReturn(trainer);

            Trainer result = trainerService.getTrainer(1L);

            assertThat(result).isEqualTo(trainer);
        }

        @Test
        @DisplayName("Should return null when trainer not found")
        void shouldReturnNullWhenTrainerNotFound() {
            when(trainerDao.findById(999L)).thenReturn(null);

            Trainer result = trainerService.getTrainer(999L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getAllTrainers()")
    class GetAllTrainersTests {

        @Test
        @DisplayName("Should return all trainers from DAO")
        void shouldReturnAllTrainersFromDao() {
            List<Trainer> trainers = Arrays.asList(
                    buildTrainer(1L, "Mike", "Smith", "YOGA"),
                    buildTrainer(2L, "Sara", "Jones", "CARDIO")
            );
            when(trainerDao.findAll()).thenReturn(trainers);

            List<Trainer> result = trainerService.getAllTrainers();

            assertThat(result).hasSize(2);
        }
    }
}
