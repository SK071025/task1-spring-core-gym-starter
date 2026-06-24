package com.example.gym.daos;

import com.example.gym.models.Trainee;
import java.util.*;

public interface TraineeDao extends BaseDao<Trainee> {
    Optional<Trainee> findByUsername(String username);
    List<Trainee> findByFirstNameAndLastName(String firstName, String lastName);
}