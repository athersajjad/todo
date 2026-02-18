package com.migration.todo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.migration.todo.TodoList;
import com.migration.todo.TodoServiceImpl;

/**
 * REST Controller for TodoList CRUD operations.
 * Replaces GWT RPC TodoService list methods.
 */
@RestController
@RequestMapping("/lists")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
public class TodoListController {

	private final TodoServiceImpl todoService;

	public TodoListController() {
		this.todoService = new TodoServiceImpl();
	}

	/**
	 * Get all todo lists
	 */
	@GetMapping
	public ResponseEntity<List<TodoList>> getAllTodoLists() {
		try {
			List<TodoList> lists = todoService.getAllTodoLists();
			return ResponseEntity.ok(lists);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Create a new todo list
	 */
	@PostMapping
	public ResponseEntity<TodoList> createTodoList(@RequestBody CreateListRequest request) {
		try {
			if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
				return ResponseEntity.badRequest().build();
			}
			TodoList newList = todoService.createTodoList(request.getName());
			return ResponseEntity.status(HttpStatus.CREATED).body(newList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Delete a todo list by ID
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTodoList(@PathVariable("id") long id) {
		try {
			boolean deleted = todoService.deleteTodoList(id);
			if (deleted) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
