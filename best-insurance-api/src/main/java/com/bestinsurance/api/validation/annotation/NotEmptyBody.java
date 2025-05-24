package com.bestinsurance.api.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyBodyValidator.class)
@Documented
public @interface NotEmptyBody {
    String message() default  "Cannot be empty, at least one field must be provided in the request body";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
