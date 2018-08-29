package com.example.demo.screen;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ScreenController {

	private ScreenRepository repository;
	private ScreenResourceAssembler assembler;
	
	@Autowired
	public ScreenController(ScreenRepository repository, ScreenResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping("/screens")
	Resources<Resource<Screen>> all() {
		List<Resource<Screen>> screens = repository.findAll().stream()
			.map(assembler::toResource)
			.collect(Collectors.toList());
		return new Resources<>(screens,
			linkTo(methodOn(ScreenController.class).all()).withSelfRel());
	}
}
