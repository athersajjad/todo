package com.migration.todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

/**
 * The server side implementation of the TodoService RPC service.
 */
@SuppressWarnings("serial")
public class TodoServiceImpl extends RemoteServiceServlet implements TodoService {

	// ==================== TodoList Operations ====================

	@Override
	public List<TodoList> getAllTodoLists() {
		List<TodoList> lists = new ArrayList<>();
		String sql = "SELECT id, name FROM todo_lists ORDER BY id";

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				TodoList list = new TodoList(
						resultSet.getLong("id"),
						resultSet.getString("name"));
				lists.add(list);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving todo lists: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeResultSet(resultSet);
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return lists;
	}

	@Override
	public TodoList createTodoList(String name) {
		String sql = "INSERT INTO todo_lists (name) VALUES (?)";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, name);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				return new TodoList(id, name);
			}
		} catch (SQLException e) {
			System.err.println("Error creating todo list: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeResultSet(resultSet);
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return null;
	}

	@Override
	public boolean deleteTodoList(long listId) {
		String sql = "DELETE FROM todo_lists WHERE id = ?";

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, listId);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting todo list: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return false;
	}

	// ==================== TodoItem Operations ====================

	@Override
	public List<TodoItem> getTodoItemsByListId(long listId) {
		List<TodoItem> items = new ArrayList<>();
		String sql = "SELECT id, list_id, title, description, completed FROM todo_items WHERE list_id = ? ORDER BY id";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, listId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TodoItem item = new TodoItem(
						resultSet.getLong("id"),
						resultSet.getLong("list_id"),
						resultSet.getString("title"),
						resultSet.getString("description"),
						resultSet.getBoolean("completed"));
				items.add(item);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving todos for list: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeResultSet(resultSet);
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return items;
	}

	@Override
	public TodoItem getTodoById(long id) {
		String sql = "SELECT id, list_id, title, description, completed FROM todo_items WHERE id = ?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return new TodoItem(
						resultSet.getLong("id"),
						resultSet.getLong("list_id"),
						resultSet.getString("title"),
						resultSet.getString("description"),
						resultSet.getBoolean("completed"));
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving todo by id: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeResultSet(resultSet);
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return null;
	}

	@Override
	public TodoItem createTodo(long listId, String title, String description, boolean completed) {
		String sql = "INSERT INTO todo_items (list_id, title, description, completed) VALUES (?, ?, ?, ?)";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, listId);
			statement.setString(2, title);
			statement.setString(3, description);
			statement.setBoolean(4, completed);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				return new TodoItem(id, listId, title, description, completed);
			}
		} catch (SQLException e) {
			System.err.println("Error creating todo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeResultSet(resultSet);
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return null;
	}

	@Override
	public TodoItem updateTodo(long id, String title, String description, boolean completed) {
		TodoItem item = getTodoById(id);
		if (item == null) {
			return null;
		}

		String sql = "UPDATE todo_items SET title = ?, description = ?, completed = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			statement.setString(2, description);
			statement.setBoolean(3, completed);
			statement.setLong(4, id);
			statement.executeUpdate();

			return new TodoItem(id, item.getListId(), title, description, completed);
		} catch (SQLException e) {
			System.err.println("Error updating todo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return null;
	}

	@Override
	public boolean deleteTodo(long id) {
		String sql = "DELETE FROM todo_items WHERE id = ?";

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = TodoDatabase.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting todo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			TodoDatabase.closeStatement(statement);
			TodoDatabase.closeConnection(connection);
		}

		return false;
	}

	@Override
	public TodoItem toggleTodoCompletion(long id) {
		TodoItem item = getTodoById(id);
		if (item != null) {
			return updateTodo(id, item.getTitle(), item.getDescription(), !item.isCompleted());
		}
		return null;
	}
}
