package com.example.suitability.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.suitability.dto.response.ErrorResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleValidationError(
			MethodArgumentNotValidException e) {
		
		List<String> messages = e.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.toList();
		
		return new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				messages
				);
	}
	
	@ExceptionHandler(HorseNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerHorseNotFound(
			HorseNotFoundException e) {
		
		return new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				List.of(e.getMessage())
				);
	}
	
	@ExceptionHandler(HorseAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleHorseAlreadyExists(
			HorseAlreadyExistsException e) {
		
		return new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				List.of(e.getMessage())
				);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleConstraintViolation(
			ConstraintViolationException e) {
		
		List<String> messages = e.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.toList();
		
		return new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				messages
				);
	}

}
