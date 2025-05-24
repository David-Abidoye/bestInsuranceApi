package com.bestinsurance.api.validation.annotation;

import java.lang.reflect.Field;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotEmptyBodyValidator implements ConstraintValidator<NotEmptyBody, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        if (obj == null){
            return false;
        }

        for (Field field : obj.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null){
                    return true;
                }
            } catch (IllegalAccessException e) {
                log.debug("Object field {} is inaccessible", field);
            }
        }
        return false;
    }
}
