package com.migration.todo.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Modal dialog for adding a new todo item to a todo list.
 * Displays form fields for title and description, with Save/Cancel buttons.
 */
public class AddItemDialog extends DialogBox {
	
	private final TextBox titleField;
	private final TextArea descriptionField;
	private final Button saveButton;
	private final Button cancelButton;
	
	private Callback callback;
	private long listId;
	
	public interface Callback {
		void onSave(String title, String description, long listId);
		void onCancel();
	}
	
	public AddItemDialog() {
		super(false, true); // autohide=false, modal=true
		setText("Add New Item");
		
		// Create form layout
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setStyleName("add-item-dialog");
		
		// Title field
		mainPanel.add(new Label("Title:"));
		titleField = new TextBox();
		titleField.setWidth("300px");
		mainPanel.add(titleField);
		
		// Description field
		mainPanel.add(new Label("Description:"));
		descriptionField = new TextArea();
		descriptionField.setWidth("300px");
		descriptionField.setHeight("100px");
		mainPanel.add(descriptionField);
		
		// Buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		
		saveButton = new Button("Save");
		saveButton.setStyleName("todo-button");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		buttonPanel.add(saveButton);
		
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName("todo-button");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onCancel();
			}
		});
		buttonPanel.add(cancelButton);
		
		mainPanel.add(buttonPanel);
		setWidget(mainPanel);
	}
	
	/**
	 * Show the dialog for adding an item to a specific list
	 */
	public void show(long listId, Callback callback) {
		this.listId = listId;
		this.callback = callback;
		
		// Clear fields
		titleField.setText("");
		descriptionField.setText("");
		
		// Focus on title field
		titleField.setFocus(true);
		
		// Center and show dialog
		center();
		super.show();
	}
	
	private void onSave() {
		String title = titleField.getText().trim();
		String description = descriptionField.getText().trim();
		
		if (title.isEmpty()) {
			// Show error - title is required
			return;
		}
		
		if (callback != null) {
			callback.onSave(title, description, listId);
		}
		
		hide();
	}
	
	private void onCancel() {
		if (callback != null) {
			callback.onCancel();
		}
		hide();
	}
	
	public String getTitle() {
		return titleField.getText().trim();
	}
	
	public String getDescription() {
		return descriptionField.getText().trim();
	}
	
	public long getListId() {
		return listId;
	}
}
