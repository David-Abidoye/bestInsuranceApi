package com.bestinsurance.api.mapper;

public interface DTOMapper<I, O> {
    O map(I i);
}
