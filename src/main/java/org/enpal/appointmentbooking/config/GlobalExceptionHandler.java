package org.enpal.appointmentbooking.config;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed for the provided request data."
        );

        List<FieldErrorDetail> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ProblemDetail handleInvalidFormatException(InvalidFormatException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Data deserialization failed."
        );

        List<FieldErrorDetail> errors = new ArrayList<>();

        // Get the field name causing the issue
        String fieldName = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .findFirst()
                .orElse("Unknown field");

        String errorMessage = "Invalid value provided. Expected format: yyyy-MM-dd for dates.";
        errors.add(new FieldErrorDetail(fieldName, errorMessage));

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }
}

record FieldErrorDetail(String field, String message) {}
