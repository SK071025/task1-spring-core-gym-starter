package com.example.gym.daos;

import com.example.gym.models.Trainer;
import com.example.gym.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TrainerDaoImpl extends BaseDaoImpl<Trainer> implements TrainerDao {

    @Autowired
    @Qualifier("trainerStorage")
    private Map<Long, Trainer> trainerStorage;

    @Override
    protected Map<Long, Trainer> getStorage() {
        return trainerStorage;
    }

    @Override
    protected void setId(Trainer entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long getId(Trainer entity) {
        return entity.getId();
    }

    @Override
    protected Long generateNextId() {
        return IdGenerator.nextTrainerId();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        logger.debug("Finding trainer by username: {}", username);
        return trainerStorage.values().stream()
                .filter(trainer -> username.equals(trainer.getUsername()))
                .findFirst();
    }

    @Override
    public List<Trainer> findBySpecialization(String specialization) {
        logger.debug("Finding trainers by specialization: {}", specialization);
        return trainerStorage.values().stream()
                .filter(trainer -> specialization.equals(trainer.getSpecialization()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Finding trainers by name: {} {}", firstName, lastName);
        return trainerStorage.values().stream()
                .filter(trainer -> firstName.equals(trainer.getFirstName()) &&
                        lastName.equals(trainer.getLastName()))
                .collect(Collectors.toList());
    }
}
