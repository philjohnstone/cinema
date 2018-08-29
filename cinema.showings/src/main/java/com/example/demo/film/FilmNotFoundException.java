package com.example.demo.film;

public class FilmNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FilmNotFoundException(long id) {
		super(String.format("Could not find film with id %d", id));
	}
}
