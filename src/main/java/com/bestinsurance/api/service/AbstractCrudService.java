package com.bestinsurance.api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bestinsurance.api.model.DomainObject;

public abstract class AbstractCrudService<T extends DomainObject<I>, I> implements CrudService<T, I> {

    protected abstract JpaRepository<T, I> getRepository();

    public T create(T t) {
        return getRepository().save(t);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public Optional<T> getById(I id) {
        return getRepository().findById(id);
    }

    public T getReferenceById(I id) {
        return getRepository().getReferenceById(id);
    }

    public void delete(I id) {
        getRepository().deleteById(id);
    }
}
