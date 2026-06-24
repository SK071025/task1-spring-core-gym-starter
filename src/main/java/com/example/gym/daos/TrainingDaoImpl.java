package com.example.gym.daos;

import com.example.gym.models.Training;
import com.example.gym.models.TrainingType;
import com.example.gym.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TrainingDaoImpl extends BaseDaoImpl<Training> implements TrainingDao {

    @Autowired
    @Qualifier("trainingStorage")
    private Map<Long, Training> trainingStorage;

    @Override
    protected Map<Long, Training> getStorage() {
        return trainingStorage;
    }

    @Override
    protected void setId(Training entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long getId(Training entity) {
        return entity.getId();
    }

    @Override
    protected Long generateNextId() {
        return IdGenerator.nextTrainingId(); // Use your IdGenerator
    }

    @Override
    public List<Training> findByTraineeId(Long traineeId) {
        logger.debug("Finding trainings by trainee ID: {}", traineeId);
        return trainingStorage.values().stream()
                .filter(training -> traineeId.equals(training.getTraineeId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Training> findByTrainerId(Long trainerId) {
        logger.debug("Finding trainings by trainer ID: {}", trainerId);
        return trainingStorage.values().stream()
                .filter(training -> trainerId.equals(training.getTrainerId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Training> findByTrainingType(TrainingType trainingType) {
        logger.debug("Finding trainings by type: {}", trainingType);
        return trainingStorage.values().stream()
                .filter(training -> trainingType.equals(training.getTrainingType()))
                .collect(Collectors.toList());
    }
}
