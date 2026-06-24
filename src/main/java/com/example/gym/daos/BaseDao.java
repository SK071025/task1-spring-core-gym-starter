package com.example.gym.daos;

import java.util.List;

public interface BaseDao<T> {
    void save(T entity);
    T findById(Long id);
    List<T> findAll();
    void delete(Long id);
    boolean existsById(Long id);
}