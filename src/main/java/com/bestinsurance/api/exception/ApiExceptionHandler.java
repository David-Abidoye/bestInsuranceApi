package com.bestinsurance.api.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bestinsurance.api.exception.dto.ApiErrorResponse;
import com.bestinsurance.api.exception.dto.ValidationError;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    //@ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //public ApiErrorResponse rootErrorHandler(Exception ex, HttpServletRequest request) {
    //    return ApiErrorResponse.builder()
    //            .message("An unexpected error occurred: " + ex.getMessage())
    //            .cause(ex.getCause() != null ? ex.getCause().getClass().toString() : null)
    //            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
    //            .timestamp(LocalDateTime.now())
    //            .path(request.getRequestURI())
    //            .build();
    //}

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse notFoundHandler(EntityNotFoundException ex, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .message("Resource not found: " + ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse validationHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .errors(getValidationErrors(ex.getBindingResult()))
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse illegalArgumentHandler(IllegalArgumentException ex, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .message("Invalid input: " + ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse constraintViolationHandler(ConstraintViolationException ex, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .message("Constraint violation")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .errors(ex.getConstraintViolations().stream()
                        .map(v -> new ValidationError(v.getPropertyPath().toString(), v.getMessage()))
                        .toList())
                .build();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse entityAlreadyExistsHandler(EntityExistsException ex){
        return ApiErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private List<ValidationError> getValidationErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
