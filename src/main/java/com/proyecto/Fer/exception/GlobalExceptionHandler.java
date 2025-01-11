package com.proyecto.Fer.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class) // se coloca la excepcion que se detecta
	public ResponseEntity<Error> recursoNoEncontrado(Exception ex) {
		Error error = new Error();
		error.setMessage(ex.getMessage());
		error.setError("Error ...recurso no encontrado");
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setDate(new Date());
		return ResponseEntity.badRequest().body(error);
	}
}
