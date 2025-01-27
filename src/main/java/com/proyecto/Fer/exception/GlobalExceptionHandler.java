package com.proyecto.Fer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> recursoNoEncontrado(ResourceNotFoundException ex, WebRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setError("Recurso no encontrado");
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setDate(new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Error errorResponse = new Error();
        errorResponse.setMessage("Validación fallida");
        errorResponse.setError("Error de validación");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setDate(new Date());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorResponse);
        response.put("validationErrors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneralException(Exception ex, WebRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setError("Error interno del servidor");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDate(new Date());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}