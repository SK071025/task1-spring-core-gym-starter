package com.example.gym.daos;

import com.example.gym.models.Trainee;
import com.example.gym.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TraineeDaoImpl extends BaseDaoImpl<Trainee> implements TraineeDao {

    @Autowired
    @Qualifier("traineeStorage")
    private Map<Long, Trainee> traineeStorage;

    @Override
    protected Map<Long, Trainee> getStorage() {
        return traineeStorage;
    }

    @Override
    protected void setId(Trainee entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long getId(Trainee entity) {
        return entity.getId();
    }

    @Override
    protected Long generateNextId() {
        return IdGenerator.nextTraineeId();
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        logger.debug("Finding trainee by username: {}", username);
        return traineeStorage.values().stream()
                .filter(trainee -> username.equals(trainee.getUsername()))
                .findFirst();
    }

    @Override
    public List<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Finding trainees by name: {} {}", firstName, lastName);
        return traineeStorage.values().stream()
                .filter(trainee -> firstName.equals(trainee.getFirstName()) &&
                        lastName.equals(trainee.getLastName()))
                .collect(Collectors.toList());
    }
}

