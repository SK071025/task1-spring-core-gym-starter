package com.example.gym.daos;

import com.example.gym.models.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerDao extends BaseDao<Trainer> {
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findBySpecialization(String specialization);
    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
}