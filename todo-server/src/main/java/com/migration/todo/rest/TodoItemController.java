package com.migration.todo.rest;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.migration.todo.TodoItem;
import com.migration.todo.TodoServiceImpl;

/**
 * REST Controller for TodoItem operations.
 * Replaces GWT RPC TodoService item methods.
 */
@RestController
@RequestMapping("/items")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
public class TodoItemController {

	private final TodoServiceImpl todoService;

	public TodoItemController() {
		this.todoService = new TodoServiceImpl();
	}

	/**
	 * Get all items for a specific list
	 */
	@GetMapping("/list/{listId}")
	public List<TodoItem> getTodoItemsByListId(@PathVariable("listId") long listId) {
		return todoService.getTodoItemsByListId(listId);
	}

	/**
	 * Get a specific todo item by ID
	 */
	@GetMapping("/{id}")
	public TodoItem getTodoById(@PathVariable("id") long id) {
		return todoService.getTodoById(id);
	}

	/**
	 * Create a new todo item
	 */
	@PostMapping
	public TodoItem createTodo(@RequestBody CreateItemRequest request) {
		return todoService.createTodo(
			request.getListId(),
			request.getTitle(),
			request.getDescription(),
			request.isCompleted()
		);
	}

	/**
	 * Update an existing todo item
	 */
	@PutMapping("/{id}")
	public TodoItem updateTodo(@PathVariable("id") long id, @RequestBody UpdateItemRequest request) {
		return todoService.updateTodo(
			id,
			request.getTitle(),
			request.getDescription(),
			request.isCompleted()
		);
	}

	/**
	 * Delete a todo item
	 */
	@DeleteMapping("/{id}")
	public boolean deleteTodo(@PathVariable("id") long id) {
		return todoService.deleteTodo(id);
	}

	/**
	 * Toggle completion status of a todo item
	 */
	@PutMapping("/{id}/toggle")
	public TodoItem toggleTodoCompletion(@PathVariable("id") long id) {
		return todoService.toggleTodoCompletion(id);
	}

	/**
	 * DTO for create item request
	 */
	public static class CreateItemRequest {
		private long listId;
		private String title;
		private String description;
		private boolean completed;

		public CreateItemRequest() {
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
	}

	/**
	 * DTO for update item request
	 */
	public static class UpdateItemRequest {
		private String title;
		private String description;
		private boolean completed;

		public UpdateItemRequest() {
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
	}
}
