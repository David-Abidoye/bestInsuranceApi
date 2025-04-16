package com.bestinsurance.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public interface CrudController<C, U, S> {

    @PostMapping
    S create(@Valid @RequestBody C c);

    @GetMapping("/{id}")
    S searchById(@NotBlank(message = "Id cannot be blank")
                 @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
                 @PathVariable String id);

    @GetMapping
    List<S> all();

    @PutMapping("/{id}")
    S update(@NotBlank(message = "Id cannot be blank")
             @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
             @PathVariable String id, @Valid @RequestBody U u);

    @DeleteMapping("/{id}")
    void delete(@NotBlank(message = "Id cannot be blank")
                @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
                @PathVariable String id);
}
