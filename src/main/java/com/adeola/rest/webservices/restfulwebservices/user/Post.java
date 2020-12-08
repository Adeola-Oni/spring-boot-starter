package com.adeola.rest.webservices.restfulwebservices.user;

public class Post {
	private Integer id;
	
	private String description;
	

	public Post(Integer id2, String description2) {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", description=" + description + "]";
	}

	
	
	

}
