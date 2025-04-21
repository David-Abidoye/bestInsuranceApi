package com.bestinsurance.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

public interface CrudController<C, U, S> {

    S create(@Valid @RequestBody C c);

    S searchById(Map<String, String> id);

    List<S> all();

    S update(Map<String, String> id, @Valid @RequestBody U u);

    void delete(Map<String, String> id);
}
