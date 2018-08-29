package com.example.demo.film;

import static java.lang.String.format;

public class FilmNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	FilmNotFoundException(long id) {
		super(format("Could not find film with id %d", id));
	}
}
