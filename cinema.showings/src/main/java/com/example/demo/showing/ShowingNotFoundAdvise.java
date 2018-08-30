package com.example.demo.showing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ShowingNotFoundAdvise {

	@ResponseBody
	@ExceptionHandler(ShowingNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String showingNotFoundHandler(ShowingNotFoundException e) {
		return e.getMessage();
	}
}
