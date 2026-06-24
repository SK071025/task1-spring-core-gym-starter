package com.example.gym;

import com.example.gym.daos.TraineeDao;
import com.example.gym.daos.TrainerDao;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.User;
import com.example.gym.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService.setTraineeDao(traineeDao);
        authenticationService.setTrainerDao(trainerDao);
    }

    private Trainee buildTrainee(String username, String password) {
        return Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username(username)
                .password(password)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();
    }

    private Trainer buildTrainer(String username, String password) {
        return Trainer.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Smith")
                .username(username)
                .password(password)
                .isActive(true)
                .specialization("YOGA")
                .build();
    }

    @Nested
    @DisplayName("authenticate()")
    class AuthenticateTests {

        @Test
        @DisplayName("Should authenticate trainee with correct credentials")
        void shouldAuthenticateTraineeWithCorrectCredentials() {
            Trainee trainee = buildTrainee("john.doe", "correctPass");
            when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

            User result = authenticationService.authenticate("john.doe", "correctPass");

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Trainee.class);
            assertThat(result.getUsername()).isEqualTo("john.doe");
        }

        @Test
        @DisplayName("Should not authenticate trainee with wrong password")
        void shouldNotAuthenticateTraineeWithWrongPassword() {
            Trainee trainee = buildTrainee("john.doe", "correctPass");
            when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
            when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

            User result = authenticationService.authenticate("john.doe", "wrongPass");

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should authenticate trainer with correct credentials")
        void shouldAuthenticateTrainerWithCorrectCredentials() {
            Trainer trainer = buildTrainer("mike.smith", "trainerPass");
            when(traineeDao.findByUsername("mike.smith")).thenReturn(Optional.empty());
            when(trainerDao.findByUsername("mike.smith")).thenReturn(Optional.of(trainer));

            User result = authenticationService.authenticate("mike.smith", "trainerPass");

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Trainer.class);
            assertThat(result.getUsername()).isEqualTo("mike.smith");
        }

        @Test
        @DisplayName("Should not authenticate trainer with wrong password")
        void shouldNotAuthenticateTrainerWithWrongPassword() {
            Trainer trainer = buildTrainer("mike.smith", "trainerPass");
            when(traineeDao.findByUsername("mike.smith")).thenReturn(Optional.empty());
            when(trainerDao.findByUsername("mike.smith")).thenReturn(Optional.of(trainer));

            User result = authenticationService.authenticate("mike.smith", "wrongPass");

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null when username not found in either DAO")
        void shouldReturnNullWhenUsernameNotFound() {
            when(traineeDao.findByUsername("unknown")).thenReturn(Optional.empty());
            when(trainerDao.findByUsername("unknown")).thenReturn(Optional.empty());

            User result = authenticationService.authenticate("unknown", "anyPass");

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should check trainee before trainer")
        void shouldCheckTraineeBeforeTrainer() {
            Trainee trainee = buildTrainee("john.doe", "pass");
            when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

            authenticationService.authenticate("john.doe", "pass");

            verify(traineeDao).findByUsername("john.doe");
            verify(trainerDao, never()).findByUsername(any());
        }
    }
}
