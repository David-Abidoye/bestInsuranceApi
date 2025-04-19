package com.bestinsurance.api.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.bestinsurance.api.mapper.DTOMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

public abstract class AbstractSimpleIdCrudController<C, U, S, T> extends AbstractCrudController<C, U, S, T, UUID> {

    @Override
    @PutMapping(value = "/{id}", consumes = MEDIA_TYPE_JSON, produces = MEDIA_TYPE_JSON)
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public S update(@PathVariable Map<String, String> idDTO, U updateDTO) {
        return super.update(idDTO, updateDTO);
    }

    @Override
    @DeleteMapping("/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"), required = true)
    public void delete(@PathVariable Map<String, String> idDTO) {
        super.delete(idDTO);
    }

    @Override
    protected DTOMapper<Map<String, String>, UUID> getIdMapper() {
        return idMap -> {
            String id = Optional.ofNullable(idMap.get("id")).orElseThrow(() -> new IllegalArgumentException("Id cannot be null"));
            if (id.isBlank() || !id.matches(UUID_PATTERN)) {
                throw new IllegalArgumentException("Invalid UUID format");
            }
            return UUID.fromString(id);
        };
    }
}
