package com.migration.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializes and manages the SQLite database for the todo application.
 */
public class TodoDatabase {
	private static final String DB_FILE = "todo.db";
	private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

	/**
	 * Initialize the database connection and create tables if they don't exist.
	 */
	public static void initializeDatabase() {
		try {
			// Load SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Create connection to database file
			try (Connection connection = DriverManager.getConnection(DB_URL)) {
				System.out.println("Database connection established: " + DB_FILE);

				// Create tables if they don't exist
				createTablesIfNotExist(connection);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("SQLite JDBC driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("Error connecting to database");
			e.printStackTrace();
		}
	}

	/**
	 * Create tables if they don't already exist.
	 */
	private static void createTablesIfNotExist(Connection connection) throws SQLException {
		// Create todo_lists table
		String createListsTableSQL = "CREATE TABLE IF NOT EXISTS todo_lists (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT NOT NULL UNIQUE, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
				")";

		// Create todo_items table with foreign key to todo_lists
		String createItemsTableSQL = "CREATE TABLE IF NOT EXISTS todo_items (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"list_id INTEGER NOT NULL, " +
				"title TEXT NOT NULL, " +
				"description TEXT, " +
				"completed INTEGER DEFAULT 0, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"FOREIGN KEY(list_id) REFERENCES todo_lists(id) ON DELETE CASCADE" +
				")";

		try (Statement statement = connection.createStatement()) {
			statement.execute(createListsTableSQL);
			System.out.println("Table 'todo_lists' ready (created or already exists)");

			statement.execute(createItemsTableSQL);
			System.out.println("Table 'todo_items' ready (created or already exists)");

			// Create index for better query performance
			String createIndexSQL = "CREATE INDEX IF NOT EXISTS idx_list_id ON todo_items(list_id)";
			statement.execute(createIndexSQL);
			
			String createCompletedIndexSQL = "CREATE INDEX IF NOT EXISTS idx_completed ON todo_items(completed)";
			statement.execute(createCompletedIndexSQL);
		}
	}

	/**
	 * Get a new database connection.
	 */
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new SQLException("SQLite JDBC driver not found", e);
		}
		return DriverManager.getConnection(DB_URL);
	}

	/**
	 * Check if the database file exists.
	 */
	public static boolean databaseExists() {
		java.io.File dbFile = new java.io.File(DB_FILE);
		return dbFile.exists();
	}

	/**
	 * Close a database connection safely.
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error closing database connection: " + e.getMessage());
			}
		}
	}

	/**
	 * Close a statement safely.
	 */
	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.err.println("Error closing statement: " + e.getMessage());
			}
		}
	}

	/**
	 * Close a result set safely.
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.err.println("Error closing result set: " + e.getMessage());
			}
		}
	}
}
