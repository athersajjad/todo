package com.migration.todo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("todoService")
public interface TodoService extends RemoteService {
	
	// TodoList operations
	
	/**
	 * Get all todo lists.
	 */
	List<TodoList> getAllTodoLists();
	
	/**
	 * Create a new todo list.
	 */
	TodoList createTodoList(String name);
	
	/**
	 * Delete a todo list and all its items.
	 */
	boolean deleteTodoList(long listId);

	// TodoItem operations
	
	/**
	 * Get all todo items for a specific list.
	 */
	List<TodoItem> getTodoItemsByListId(long listId);
	
	/**
	 * Get a specific todo item by ID.
	 */
	TodoItem getTodoById(long id);
	
	/**
	 * Create a new todo item in a list.
	 */
	TodoItem createTodo(long listId, String title, String description, boolean completed);
	
	/**
	 * Update an existing todo item.
	 */
	TodoItem updateTodo(long id, String title, String description, boolean completed);
	
	/**
	 * Delete a todo item.
	 */
	boolean deleteTodo(long id);
	
	/**
	 * Toggle completion status of a todo item.
	 */
	TodoItem toggleTodoCompletion(long id);
}
