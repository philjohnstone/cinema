package com.example.demo.screen;

import static java.lang.String.format;

public class ScreenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	ScreenNotFoundException(long id) {
		super(format("Could not find screen with id %s", id));
	}
}
