package com.migration.todo;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Application lifecycle listener that initializes the database on startup.
 */
public class AppInitializationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Initializing Todo application...");
		TodoDatabase.initializeDatabase();
		System.out.println("Todo application initialized successfully");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Todo application shutting down...");
	}
}
