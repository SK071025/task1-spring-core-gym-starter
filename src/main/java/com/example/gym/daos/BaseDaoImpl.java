package com.example.gym.daos;

import com.example.gym.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract Map<Long, T> getStorage();
    protected abstract void setId(T entity, Long id);
    protected abstract Long getId(T entity);
    protected abstract Long generateNextId();

    @Override
    public void save(T entity) {
        if (getId(entity) == null) {
            Long newId = generateNextId();
            setId(entity, newId);
        }
        getStorage().put(getId(entity), entity);
        logger.info("Saved entity with ID: {}", getId(entity));
    }

    @Override
    public T findById(Long id) {
        logger.debug("Finding entity by ID: {}", id);
        return getStorage().get(id);
    }

    @Override
    public List<T> findAll() {
        logger.debug("Finding all entities");
        return new ArrayList<>(getStorage().values());
    }

    @Override
    public void delete(Long id) {
        T removed = getStorage().remove(id);
        if (removed != null) {
            logger.info("Deleted entity with ID: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent entity with ID: {}", id);
        }
    }

    @Override
    public boolean existsById(Long id) {
        return getStorage().containsKey(id);
    }
}