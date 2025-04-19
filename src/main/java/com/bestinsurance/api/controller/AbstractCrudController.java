package com.bestinsurance.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.bestinsurance.api.mapper.DTOMapper;
import com.bestinsurance.api.service.CrudService;

public abstract class AbstractCrudController<C, U, S, T, I> implements CrudController<C, U, S> {

    protected static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    protected static final String MEDIA_TYPE_JSON = "application/json";

    protected abstract CrudService<T, I> getService();
    protected abstract DTOMapper<C, T> getCreateDtoMapper();
    protected abstract DTOMapper<U, T> getUpdateDtoMapper();
    protected abstract DTOMapper<T, S> getSearchDtoMapper();
    protected abstract DTOMapper<Map<String, String>, I> getIdMapper();

    @Override
    @PostMapping(consumes = MEDIA_TYPE_JSON, produces = MEDIA_TYPE_JSON)
    public S create(C createDTO) {
        T domainObj = getService().create(getCreateDtoMapper().map(createDTO));
        return getSearchDtoMapper().map(domainObj);
    }

    @Override
    @GetMapping(produces = MEDIA_TYPE_JSON)
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
