package com.example.gym;

import com.example.gym.daos.TrainerDaoImpl;
import com.example.gym.models.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainerDaoImpl Tests")
class TrainerDaoImplTest {

    private TrainerDaoImpl trainerDao;
    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerDao = new TrainerDaoImpl();
        trainerStorage = new HashMap<>();
        ReflectionTestUtils.setField(trainerDao, "trainerStorage", trainerStorage);
    }

    private Trainer buildTrainer(Long id, String firstName, String lastName,
                                 String username, String specialization) {
        return Trainer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password("password")
                .isActive(true)
                .specialization(specialization)
                .build();
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Should save trainer with existing ID")
        void shouldSaveTrainerWithExistingId() {
            Trainer trainer = buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA");

            trainerDao.save(trainer);

            assertThat(trainerStorage).containsKey(1L);
            assertThat(trainerStorage.get(1L)).isEqualTo(trainer);
        }

        @Test
        @DisplayName("Should auto-generate ID when trainer has no ID")
        void shouldAutoGenerateIdWhenTrainerHasNoId() {
            Trainer trainer = buildTrainer(null, "Mike", "Smith", "mike.smith", "CARDIO");

            trainerDao.save(trainer);

            assertThat(trainer.getId()).isNotNull();
            assertThat(trainerStorage).containsKey(trainer.getId());
        }
    }

    @Nested
    @DisplayName("findByUsername()")
    class FindByUsernameTests {

        @Test
        @DisplayName("Should find trainer by username")
        void shouldFindTrainerByUsername() {
            Trainer trainer = buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA");
            trainerStorage.put(1L, trainer);

            Optional<Trainer> result = trainerDao.findByUsername("mike.smith");

            assertThat(result).isPresent();
            assertThat(result.get().getUsername()).isEqualTo("mike.smith");
        }

        @Test
        @DisplayName("Should return empty when username not found")
        void shouldReturnEmptyWhenUsernameNotFound() {
            Optional<Trainer> result = trainerDao.findByUsername("nonexistent");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySpecialization()")
    class FindBySpecializationTests {

        @Test
        @DisplayName("Should return trainers with matching specialization")
        void shouldReturnTrainersWithMatchingSpecialization() {
            trainerStorage.put(1L, buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA"));
            trainerStorage.put(2L, buildTrainer(2L, "Sara", "Jones", "sara.jones", "YOGA"));
            trainerStorage.put(3L, buildTrainer(3L, "Tom", "Brown", "tom.brown", "CARDIO"));

            List<Trainer> result = trainerDao.findBySpecialization("YOGA");

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(t -> t.getSpecialization().equals("YOGA"));
        }

        @Test
        @DisplayName("Should return empty list when no trainers match specialization")
        void shouldReturnEmptyListWhenNoTrainersMatchSpecialization() {
            List<Trainer> result = trainerDao.findBySpecialization("PILATES");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByFirstNameAndLastName()")
    class FindByNameTests {

        @Test
        @DisplayName("Should return trainers matching first and last name")
        void shouldReturnTrainersMatchingFirstAndLastName() {
            trainerStorage.put(1L, buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA"));
            trainerStorage.put(2L, buildTrainer(2L, "Mike", "Smith", "mike.smith2", "CARDIO"));
            trainerStorage.put(3L, buildTrainer(3L, "Sara", "Jones", "sara.jones", "YOGA"));

            List<Trainer> result = trainerDao.findByFirstNameAndLastName("Mike", "Smith");

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list when no match found")
        void shouldReturnEmptyListWhenNoMatchFound() {
            List<Trainer> result = trainerDao.findByFirstNameAndLastName("Unknown", "Person");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class DeleteTests {

        @Test
        @DisplayName("Should delete trainer by ID")
        void shouldDeleteTrainerById() {
            trainerStorage.put(1L, buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA"));

            trainerDao.delete(1L);

            assertThat(trainerStorage).doesNotContainKey(1L);
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("Should return true when trainer exists")
        void shouldReturnTrueWhenTrainerExists() {
            trainerStorage.put(1L, buildTrainer(1L, "Mike", "Smith", "mike.smith", "YOGA"));

            assertThat(trainerDao.existsById(1L)).isTrue();
        }

        @Test
        @DisplayName("Should return false when trainer does not exist")
        void shouldReturnFalseWhenTrainerDoesNotExist() {
            assertThat(trainerDao.existsById(999L)).isFalse();
        }
    }
}

