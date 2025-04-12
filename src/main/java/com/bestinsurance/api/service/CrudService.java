package com.bestinsurance.api.service;

import java.util.List;
import java.util.Optional;
public interface CrudService<T> {

    T create(T t);
    List<T> findAll();
    Optional<T> getById(String id);
    T getReferenceById(String id);
    T update(String id, T t);
    void delete(String id);
}
