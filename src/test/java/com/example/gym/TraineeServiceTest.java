package com.example.gym;

import com.example.gym.daos.TraineeDao;
import com.example.gym.models.Trainee;
import com.example.gym.services.CredentialsService;
import com.example.gym.services.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TraineeService Tests")
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeService.setTraineeDao(traineeDao);
        traineeService.setUsernamePasswordGenerator(credentialsService);
    }

    private Trainee buildTrainee(Long id, String firstName, String lastName) {
        return Trainee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();
    }

    @Nested
    @DisplayName("createTrainee()")
    class CreateTraineeTests {

        @Test
        @DisplayName("Should create trainee with generated username and password")
        void shouldCreateTraineeWithGeneratedUsernameAndPassword() {
            Trainee trainee = buildTrainee(null, "John", "Doe");
            when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
            when(credentialsService.generatePassword()).thenReturn("securePass1");

            Trainee result = traineeService.createTrainee(trainee);

            assertThat(result.getUsername()).isEqualTo("John.Doe");
            assertThat(result.getPassword()).isEqualTo("securePass1");
            verify(traineeDao, times(1)).save(trainee);
        }

        @Test
        @DisplayName("Should append serial number when username already exists")
        void shouldAppendSerialNumberWhenUsernameAlreadyExists() {
            Trainee trainee = buildTrainee(null, "John", "Doe");
            Trainee existingTrainee = buildTrainee(1L, "John", "Doe");
            existingTrainee.setUsername("John.Doe");

            when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(existingTrainee));
            when(traineeDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
            when(credentialsService.generatePassword()).thenReturn("securePass2");

            Trainee result = traineeService.createTrainee(trainee);

            assertThat(result.getUsername()).isEqualTo("John.Doe1");
            verify(traineeDao).save(trainee);
        }

        @Test
        @DisplayName("Should increment serial number until unique username found")
        void shouldIncrementSerialNumberUntilUniqueUsernameFound() {
            Trainee trainee = buildTrainee(null, "John", "Doe");
            Trainee existing1 = buildTrainee(1L, "John", "Doe");
            Trainee existing2 = buildTrainee(2L, "John", "Doe");

            when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(existing1));
            when(traineeDao.findByUsername("John.Doe1")).thenReturn(Optional.of(existing2));
            when(traineeDao.findByUsername("John.Doe2")).thenReturn(Optional.empty());
            when(credentialsService.generatePassword()).thenReturn("pass");

            Trainee result = traineeService.createTrainee(trainee);

            assertThat(result.getUsername()).isEqualTo("John.Doe2");
        }
    }

    @Nested
    @DisplayName("updateTrainee()")
    class UpdateTraineeTests {

        @Test
        @DisplayName("Should save and return updated trainee")
        void shouldSaveAndReturnUpdatedTrainee() {
            Trainee trainee = buildTrainee(1L, "John", "Doe");

            Trainee result = traineeService.updateTrainee(trainee);

            verify(traineeDao).save(trainee);
            assertThat(result).isEqualTo(trainee);
        }
    }

    @Nested
    @DisplayName("deleteTrainee()")
    class DeleteTraineeTests {

        @Test
        @DisplayName("Should call delete on DAO with correct ID")
        void shouldCallDeleteOnDaoWithCorrectId() {
            traineeService.deleteTrainee(1L);

            verify(traineeDao).delete(1L);
        }
    }

    @Nested
    @DisplayName("getTrainee()")
    class GetTraineeTests {

        @Test
        @DisplayName("Should return trainee from DAO by ID")
        void shouldReturnTraineeFromDaoById() {
            Trainee trainee = buildTrainee(1L, "John", "Doe");
            when(traineeDao.findById(1L)).thenReturn(trainee);

            Trainee result = traineeService.getTrainee(1L);

            assertThat(result).isEqualTo(trainee);
            verify(traineeDao).findById(1L);
        }

        @Test
        @DisplayName("Should return null when trainee not found")
        void shouldReturnNullWhenTraineeNotFound() {
            when(traineeDao.findById(999L)).thenReturn(null);

            Trainee result = traineeService.getTrainee(999L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getAllTrainees()")
    class GetAllTraineesTests {

        @Test
        @DisplayName("Should return all trainees from DAO")
        void shouldReturnAllTraineesFromDao() {
            List<Trainee> trainees = Arrays.asList(
                    buildTrainee(1L, "John", "Doe"),
                    buildTrainee(2L, "Jane", "Smith")
            );
            when(traineeDao.findAll()).thenReturn(trainees);

            List<Trainee> result = traineeService.getAllTrainees();

            assertThat(result).hasSize(2);
            verify(traineeDao).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no trainees")
        void shouldReturnEmptyListWhenNoTrainees() {
            when(traineeDao.findAll()).thenReturn(List.of());

            List<Trainee> result = traineeService.getAllTrainees();

            assertThat(result).isEmpty();
        }
    }
}
