package com.migration.todo.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Main container view with 2-panel layout:
 * Left panel: Todo lists (scrollable)
 * Right panel: Items in selected list (grid view) + Add Item button
 */
public class MainView extends Composite {

	private final FlowPanel mainPanel;
	private final TodoListView todoListView;
	private final TodoItemsGridView todoItemsGridView;

	public MainView() {
		mainPanel = new FlowPanel();
		mainPanel.setStyleName("main-container");

		// Create the left panel with scrollable list of todo lists
		todoListView = new TodoListView();
		todoListView.setStyleName("left-panel");

		// Create the right panel with grid of items from selected list
		todoItemsGridView = new TodoItemsGridView();
		todoItemsGridView.setStyleName("right-panel");

		// Assemble
		mainPanel.add(todoListView);
		mainPanel.add(todoItemsGridView);

		initWidget(mainPanel);
	}

	public TodoListView getTodoListView() {
		return todoListView;
	}

	public TodoItemsGridView getTodoItemsGridView() {
		return todoItemsGridView;
	}
}
