package com.example.demo.showing;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class ShowingResourceAssembler implements ResourceAssembler<Showing, Resource<Showing>> {

	@Override
	public Resource<Showing> toResource(Showing entity) {
		return new Resource<Showing>(entity,
			linkTo(methodOn(ShowingController.class).one(entity.getId())).withSelfRel(),
			linkTo(methodOn(ShowingController.class).all()).withRel("showings"));
	}

}
