package com.migration.todo.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * View for displaying and editing a single todo item.
 */
public class TodoDetailView extends Composite {

	private final VerticalPanel mainPanel;
	private final TextBox titleField;
	private final TextArea descriptionField;
	private final CheckBox completedCheckBox;
	private final Button saveButton;
	private final Button deleteButton;
	private final Button addItemButton;
	private final Label messageLabel;

	private long currentTodoId = -1;

	public TodoDetailView() {
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("todo-detail");
		mainPanel.setSpacing(10);

		// Title section
		Label titleLabel = new Label("Title:");
		titleLabel.setStyleName("todo-label");
		titleField = new TextBox();
		titleField.setWidth("100%");
		titleField.setStyleName("todo-input");

		// Description section
		Label descriptionLabel = new Label("Description:");
		descriptionLabel.setStyleName("todo-label");
		descriptionField = new TextArea();
		descriptionField.setWidth("100%");
		descriptionField.setHeight("150px");
		descriptionField.setStyleName("todo-input");

		// Completed checkbox
		HorizontalPanel completedPanel = new HorizontalPanel();
		completedCheckBox = new CheckBox("Completed");
		completedCheckBox.setStyleName("todo-checkbox");
		completedPanel.add(completedCheckBox);
		completedPanel.setSpacing(5);

		// Buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		saveButton = new Button("Save");
		saveButton.setStyleName("todo-button");
		deleteButton = new Button("Delete");
		deleteButton.setStyleName("todo-button delete-button");
		addItemButton = new Button("Add Item");
		addItemButton.setStyleName("todo-button");
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(addItemButton);
		buttonPanel.setSpacing(5);

		// Message label
		messageLabel = new Label("");
		messageLabel.setStyleName("todo-message");

		// Assemble
		mainPanel.add(titleLabel);
		mainPanel.add(titleField);
		mainPanel.add(descriptionLabel);
		mainPanel.add(descriptionField);
		mainPanel.add(completedPanel);
		mainPanel.add(buttonPanel);
		mainPanel.add(messageLabel);

		initWidget(mainPanel);
	}

	public void setTodoId(long id) {
		this.currentTodoId = id;
	}

	public long getTodoId() {
		return currentTodoId;
	}

	public void setTitle(String title) {
		titleField.setText(title);
	}

	public String getTitle() {
		return titleField.getText();
	}

	public void setDescription(String description) {
		descriptionField.setText(description);
	}

	public String getDescription() {
		return descriptionField.getText();
	}

	public void setCompleted(boolean completed) {
		completedCheckBox.setValue(completed);
	}

	public boolean isCompleted() {
		return completedCheckBox.getValue();
	}

	public void addSaveButtonClickHandler(ClickHandler handler) {
		saveButton.addClickHandler(handler);
	}

	public void addDeleteButtonClickHandler(ClickHandler handler) {
		deleteButton.addClickHandler(handler);
	}

	public void addAddItemButtonClickHandler(ClickHandler handler) {
		addItemButton.addClickHandler(handler);
	}

	public void showMessage(String message) {
		messageLabel.setText(message);
	}

	public void clearMessage() {
		messageLabel.setText("");
	}

	public void clear() {
		currentTodoId = -1;
		titleField.setText("");
		descriptionField.setText("");
		completedCheckBox.setValue(false);
		messageLabel.setText("");
	}

	public void setEnabled(boolean enabled) {
		titleField.setEnabled(enabled);
		descriptionField.setEnabled(enabled);
		completedCheckBox.setEnabled(enabled);
		saveButton.setEnabled(enabled);
		deleteButton.setEnabled(enabled);
	}
}
