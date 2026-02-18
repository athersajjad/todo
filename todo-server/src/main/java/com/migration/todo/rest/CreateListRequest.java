package com.migration.todo.rest;

/**
 * DTO for creating a new TodoList via REST API.
 */
public class CreateListRequest {
	private String name;

	public CreateListRequest() {
	}

	public CreateListRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
