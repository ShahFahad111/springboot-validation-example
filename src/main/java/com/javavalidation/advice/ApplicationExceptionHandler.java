package com.javavalidation.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javavalidation.exception.UserNotFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> handleInvalidArgs(MethodArgumentNotValidException ex){
		Map<String,String> errMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> {
			errMap.put(e.getField(), e.getDefaultMessage());
		});
		return errMap;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UserNotFoundException.class)
	public Map<String,String> handleBusinessException(UserNotFoundException ex){
		Map<String,String> errMap = new HashMap<>();
		errMap.put("errorMsg", ex.getMessage());
		return errMap;
	}
}
