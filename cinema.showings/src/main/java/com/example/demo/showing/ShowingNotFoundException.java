package com.example.demo.showing;

public class ShowingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ShowingNotFoundException(long id) {
		super(String.format("Could not find showing with id %d", id));
	}
}
