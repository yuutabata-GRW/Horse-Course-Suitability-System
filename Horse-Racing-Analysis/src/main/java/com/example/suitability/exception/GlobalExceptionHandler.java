package com.example.suitability.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.suitability.dto.response.ErrorResponse;

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

}
