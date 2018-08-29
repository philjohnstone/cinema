package com.example.demo.film;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FilmNotFoundAdvise {

	@ResponseBody
	@ExceptionHandler(FilmNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String filmNotFoundHandler(FilmNotFoundException e) {
		return e.getMessage();
	}
}
