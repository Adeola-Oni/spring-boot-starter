package com.adeola.rest.webservices.restfulwebservices.user;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
 

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class UserResource {
	@Autowired
	private UserDaoService service;
	
	@GetMapping(path="/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}
	
	@GetMapping(path="/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);
		
		if (user == null) {
			throw new UserNotFoundException("id-"+id);
		}
		EntityModel<User> resource = EntityModel.of(user);
		
		WebMvcLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		
		resource.add(linkTo.withRel("all-users"));
		
		//HATEOAS
		return user;
	}
	
	@DeleteMapping(path="/users/{id}")
	public User deleteUser(@PathVariable int id) {
		User user = service.deleteByID(id);
		
		if (user == null) {
			throw new UserNotFoundException("id-"+id);
		}
		
		return user;
	} 
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		
		//this is how to create the URI of the location that is being posted to
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
		
		return ResponseEntity.created(location).build();
		
	}
	
	
	@GetMapping(path="/users/{id}/posts")
	public List<Post> getPosts(@PathVariable int id) {
		User user = service.findOne(id);
		if (user == null) {
			throw new UserNotFoundException("id-"+id);
		}
		return service.findAllUserPosts(id);
	}
	
	@GetMapping(path="/users/{id}/posts/{post_id}")
	public Post getOnePost(@PathVariable int id, @PathVariable Integer post_id) {
		List<Post> posts= service.findAllUserPosts(id);
		if (posts == null) {
			throw new UserNotFoundException("id-"+id);
		}
		
		for(Post post:posts) {
			if(post.getId() == post_id) {
				return post;
			}
		}
		
		throw new UserNotFoundException("id-"+id);
	}

}
