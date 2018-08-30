package com.example.demo.film;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {

	private FilmRepository repository;
	private FilmResourceAssembler assembler;
	
	@Autowired
	FilmController(FilmRepository repository, FilmResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping("/films")
	Resources<Resource<Film>> all() {
		List<Resource<Film>> films = repository.findAll().stream()
			.map(assembler::toResource)
			.collect(Collectors.toList()); 
		return new Resources<>(films,
			linkTo(methodOn(FilmController.class).all()).withSelfRel());
	}
	
	@GetMapping("/films/{id}")
	Resource<Film> one(@PathVariable long id) {
		return assembler.toResource(repository.findById(id).orElseThrow(() -> new FilmNotFoundException(id)));
	}
}
