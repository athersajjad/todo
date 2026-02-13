package com.migration.todo;

import java.io.Serializable;

/**
 * Represents a single todo item. Must be serializable for GWT RPC.
 */
public class TodoItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long listId;
	private String title;
	private String description;
	private boolean completed;

	/**
	 * Default constructor (required for GWT serialization).
	 */
	public TodoItem() {
	}

	/**
	 * Constructor with title and description.
	 */
	public TodoItem(String title, String description) {
		this.title = title;
		this.description = description;
		this.completed = false;
	}

	/**
	 * Full constructor.
	 */
	public TodoItem(long id, long listId, String title, String description, boolean completed) {
		this.id = id;
		this.listId = listId;
		this.title = title;
		this.description = description;
		this.completed = completed;
	}

	// Getters and Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return "TodoItem{" +
				"id=" + id +
				", listId=" + listId +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", completed=" + completed +
				'}';
	}
}
