package com.example.demo.showing;

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
public class ShowingController {

	private ShowingRepository repository;
	private ShowingResourceAssembler assembler;
	
	@Autowired
	public ShowingController(ShowingRepository repository, ShowingResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping("/showings")
	Resources<Resource<Showing>> all() {
		List<Resource<Showing>> showings = repository.findAll().stream()
			.map(assembler::toResource)
			.collect(Collectors.toList());
		return new Resources<>(showings,
			linkTo(methodOn(ShowingController.class).all()).withSelfRel());
	}
	
	@GetMapping("/showings/{id}")
	Resource<Showing> one(@PathVariable long id) {
		return assembler.toResource(repository.findById(id).orElseThrow(() -> new ShowingNotFoundException(id)));
	}
}
