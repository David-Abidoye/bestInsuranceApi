package com.bestinsurance.api.controller;

import java.util.List;
import java.util.Map;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public abstract class AbstractCrudController<C, U, S, T, I> implements CrudController<C, U, S> {

    protected abstract CrudService<T, I> getService();
    protected abstract DTOMapper<C, T> getCreateDtoMapper();
    protected abstract DTOMapper<U, T> getUpdateDtoMapper();
    protected abstract DTOMapper<T, S> getSearchDtoMapper();
    protected abstract DTOMapper<Map<String, String>, I> getIdMapper();

    @Override
    public S create(C createDTO) {
        T domainObj = getService().create(getCreateDtoMapper().map(createDTO));
        return getSearchDtoMapper().map(domainObj);
    }

    @Override
    public S searchById(Map<String, String> idDTO) {
        I id = getIdMapper().map(idDTO);
        T domainObj = getService().getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id.toString() + " does not exist!"));
        return getSearchDtoMapper().map(domainObj);
    }

    @Override
    public List<S> all() {
        List<T> allDomainObjects = getService().findAll();
        return allDomainObjects.stream()
                .map(getSearchDtoMapper()::map)
                .toList();
    }

    @Override
    public S update(Map<String, String> idDTO, U updateDTO) {
        T updatedDomainObj = getService().update(getIdMapper().map(idDTO), getUpdateDtoMapper().map(updateDTO));
        return getSearchDtoMapper().map(updatedDomainObj);
    }

    @Override
    public void delete(Map<String, String> idDTO) {
        getService().delete(getIdMapper().map(idDTO));
    }
}
