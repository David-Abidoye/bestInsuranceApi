package com.bestinsurance.api.service;

import java.util.List;
import java.util.Optional;
public interface CrudService<T, I> {

    T create(T t);
    List<T> findAll();
    Optional<T> getById(I id);
    T getReferenceById(I id);
    T update(I id, T t);
    void delete(I id);
}
