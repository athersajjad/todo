package com.migration.todo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link TodoService}.
 */
public interface TodoServiceAsync {
	
	void getAllTodoLists(AsyncCallback<List<TodoList>> callback);
	
	void createTodoList(String name, AsyncCallback<TodoList> callback);
	
	void deleteTodoList(long listId, AsyncCallback<Boolean> callback);

	void getTodoItemsByListId(long listId, AsyncCallback<List<TodoItem>> callback);
	
	void getTodoById(long id, AsyncCallback<TodoItem> callback);
	
	void createTodo(long listId, String title, String description, boolean completed, AsyncCallback<TodoItem> callback);
	
	void updateTodo(long id, String title, String description, boolean completed, AsyncCallback<TodoItem> callback);
	
	void deleteTodo(long id, AsyncCallback<Boolean> callback);
	
	void toggleTodoCompletion(long id, AsyncCallback<TodoItem> callback);
}
