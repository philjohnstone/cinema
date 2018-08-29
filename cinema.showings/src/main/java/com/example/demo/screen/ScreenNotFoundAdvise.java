package com.example.demo.screen;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ScreenNotFoundAdvise {

	@ResponseBody
	@ExceptionHandler(ScreenNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String screenNotFoundHandler(ScreenNotFoundException e) {
		return e.getMessage();
	}
}
