package com.example.demo.screen;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class ScreenResourceAssembler implements ResourceAssembler<Screen, Resource<Screen>> {

	@Override
	public Resource<Screen> toResource(Screen entity) {
		return new Resource<Screen>(entity,
			linkTo(methodOn(ScreenController.class).one(entity.getId())).withSelfRel(),
			linkTo(methodOn(ScreenController.class).all()).withRel("screens"));
	}

}
