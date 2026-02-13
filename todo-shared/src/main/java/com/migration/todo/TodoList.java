package com.migration.todo;

import java.io.Serializable;

/**
 * Represents a todo list container with a name.
 * A TodoList can contain multiple TodoItems.
 */
public class TodoList implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;

	/**
	 * Default constructor (required for GWT serialization).
	 */
	public TodoList() {
	}

	/**
	 * Constructor with name.
	 */
	public TodoList(String name) {
		this.name = name;
	}

	/**
	 * Full constructor.
	 */
	public TodoList(long id, String name) {
		this.id = id;
		this.name = name;
	}

	// Getters and Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TodoList{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
