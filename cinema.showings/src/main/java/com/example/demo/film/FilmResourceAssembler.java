package com.example.demo.film;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class FilmResourceAssembler implements ResourceAssembler<Film, Resource<Film>> {

	@Override
	public Resource<Film> toResource(Film entity) {
		return new Resource<Film>(entity, 
				linkTo(methodOn(FilmController.class).one(entity.getId())).withSelfRel(),
				linkTo(methodOn(FilmController.class).all()).withRel("films"));
	}

}
