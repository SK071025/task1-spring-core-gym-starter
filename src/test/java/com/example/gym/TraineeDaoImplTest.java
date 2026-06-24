package com.example.gym;

import com.example.gym.daos.TraineeDaoImpl;
import com.example.gym.models.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("TraineeDaoImpl Tests")
class TraineeDaoImplTest {

    private TraineeDaoImpl traineeDao;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeDao = new TraineeDaoImpl();
        traineeStorage = new HashMap<>();
        ReflectionTestUtils.setField(traineeDao, "traineeStorage", traineeStorage);
    }

    private Trainee buildTrainee(Long id, String firstName, String lastName,
                                 String username, String password) {
        return Trainee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Should save trainee with existing ID")
        void shouldSaveTraineeWithExistingId() {
            Trainee trainee = buildTrainee(1L, "John", "Doe", "john.doe", "pass123");

            traineeDao.save(trainee);

            assertThat(traineeStorage).containsKey(1L);
            assertThat(traineeStorage.get(1L)).isEqualTo(trainee);
        }

        @Test
        @DisplayName("Should auto-generate ID when trainee has no ID")
        void shouldAutoGenerateIdWhenTraineeHasNoId() {
            Trainee trainee = buildTrainee(null, "Jane", "Doe", "jane.doe", "pass456");

            traineeDao.save(trainee);

            assertThat(trainee.getId()).isNotNull();
            assertThat(traineeStorage).containsKey(trainee.getId());
        }

        @Test
        @DisplayName("Should overwrite existing trainee with same ID")
        void shouldOverwriteExistingTraineeWithSameId() {
            Trainee original = buildTrainee(1L, "John", "Doe", "john.doe", "pass123");
            Trainee updated = buildTrainee(1L, "John", "Updated", "john.updated", "newpass");

            traineeDao.save(original);
            traineeDao.save(updated);

            assertThat(traineeStorage).hasSize(1);
            assertThat(traineeStorage.get(1L).getLastName()).isEqualTo("Updated");
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Should return trainee when ID exists")
        void shouldReturnTraineeWhenIdExists() {
            Trainee trainee = buildTrainee(1L, "John", "Doe", "john.doe", "pass123");
            traineeStorage.put(1L, trainee);

            Trainee result = traineeDao.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("Should return null when ID does not exist")
        void shouldReturnNullWhenIdDoesNotExist() {
            Trainee result = traineeDao.findById(999L);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Should return all trainees")
        void shouldReturnAllTrainees() {
            traineeStorage.put(1L, buildTrainee(1L, "John", "Doe", "john.doe", "pass1"));
            traineeStorage.put(2L, buildTrainee(2L, "Jane", "Smith", "jane.smith", "pass2"));

            List<Trainee> result = traineeDao.findAll();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list when storage is empty")
        void shouldReturnEmptyListWhenStorageIsEmpty() {
            List<Trainee> result = traineeDao.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class DeleteTests {

        @Test
        @DisplayName("Should delete trainee when ID exists")
        void shouldDeleteTraineeWhenIdExists() {
            traineeStorage.put(1L, buildTrainee(1L, "John", "Doe", "john.doe", "pass123"));

            traineeDao.delete(1L);

            assertThat(traineeStorage).doesNotContainKey(1L);
        }

        @Test
        @DisplayName("Should not throw when deleting non-existent ID")
        void shouldNotThrowWhenDeletingNonExistentId() {
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                    () -> traineeDao.delete(999L)
            );
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("Should return true when trainee exists")
        void shouldReturnTrueWhenTraineeExists() {
            traineeStorage.put(1L, buildTrainee(1L, "John", "Doe", "john.doe", "pass123"));

            assertThat(traineeDao.existsById(1L)).isTrue();
        }

        @Test
        @DisplayName("Should return false when trainee does not exist")
        void shouldReturnFalseWhenTraineeDoesNotExist() {
            assertThat(traineeDao.existsById(999L)).isFalse();
        }
    }

    @Nested
    @DisplayName("findByUsername()")
    class FindByUsernameTests {

        @Test
        @DisplayName("Should return trainee when username matches")
        void shouldReturnTraineeWhenUsernameMatches() {
            Trainee trainee = buildTrainee(1L, "John", "Doe", "john.doe", "pass123");
            traineeStorage.put(1L, trainee);

            Optional<Trainee> result = traineeDao.findByUsername("john.doe");

            assertThat(result).isPresent();
            assertThat(result.get().getUsername()).isEqualTo("john.doe");
        }

        @Test
        @DisplayName("Should return empty Optional when username not found")
        void shouldReturnEmptyOptionalWhenUsernameNotFound() {
            Optional<Trainee> result = traineeDao.findByUsername("unknown.user");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should be case-sensitive")
        void shouldBeCaseSensitive() {
            Trainee trainee = buildTrainee(1L, "John", "Doe", "john.doe", "pass123");
            traineeStorage.put(1L, trainee);

            Optional<Trainee> result = traineeDao.findByUsername("John.Doe");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByFirstNameAndLastName()")
    class FindByFirstNameAndLastNameTests {

        @Test
        @DisplayName("Should return matching trainees")
        void shouldReturnMatchingTrainees() {
            traineeStorage.put(1L, buildTrainee(1L, "John", "Doe", "john.doe", "pass1"));
            traineeStorage.put(2L, buildTrainee(2L, "John", "Doe", "john.doe2", "pass2"));
            traineeStorage.put(3L, buildTrainee(3L, "Jane", "Smith", "jane.smith", "pass3"));

            List<Trainee> result = traineeDao.findByFirstNameAndLastName("John", "Doe");

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(t ->
                    t.getFirstName().equals("John") && t.getLastName().equals("Doe"));
        }

        @Test
        @DisplayName("Should return empty list when no match found")
        void shouldReturnEmptyListWhenNoMatchFound() {
            List<Trainee> result = traineeDao.findByFirstNameAndLastName("Unknown", "Person");

            assertThat(result).isEmpty();
        }
    }
}