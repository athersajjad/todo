package com.migration.todo.client.ui;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.migration.todo.TodoList;
import com.migration.todo.TodoItem;

/**
 * View for displaying scrollable list of todo lists.
 * Left panel only - items and details are shown separately.
 */
public class TodoListView extends Composite {

	private final FlowPanel mainPanel;
	private final FlowPanel addListPanel;
	private final FlowPanel listsContainer;
	private final ScrollPanel listsScrollPanel;
	private final TextBox newListField;
	private final Button addListButton;
	// store selected list id internally (no hidden label)
	private long selectedListId = -1;
	private final Map<Long, Label> listItemWidgets;
	// message label removed (messages handled by other views)
	
	// Selection handler
	private ChangeHandler listSelectionHandler;

	public TodoListView() {
		mainPanel = new FlowPanel();
		mainPanel.setStyleName("todo-list-container");
		mainPanel.setWidth("100%");
		mainPanel.setHeight("100%");
		
		listItemWidgets = new HashMap<Long, Label>();

		// ===== TITLE =====
		Label listsTitle = new Label("Todo Lists");
		listsTitle.setStyleName("section-title");

		// ===== ADD LIST FORM =====
		addListPanel = new FlowPanel();
		addListPanel.setStyleName("add-list-panel");
		newListField = new TextBox();
		newListField.setTitle("Enter list name");
		newListField.setWidth("100%");
		newListField.setHeight("35px");
		newListField.setStyleName("todo-input");
		addListButton = new Button("Add");
		addListButton.setStyleName("todo-button small");
		addListButton.setHeight("35px");
		addListPanel.add(newListField);
		addListPanel.add(addListButton);

		// ===== SCROLLABLE LISTS =====
		listsContainer = new FlowPanel();
		listsContainer.setStyleName("lists-container");
		listsContainer.setWidth("100%");
		
		listsScrollPanel = new ScrollPanel(listsContainer);
		listsScrollPanel.setWidth("100%");
		listsScrollPanel.setHeight("100%");
		listsScrollPanel.setStyleName("lists-scroll-panel");

		// selectedListId stored internally

		// ===== ASSEMBLE =====
		mainPanel.add(listsTitle);
		mainPanel.add(addListPanel);
		mainPanel.add(listsScrollPanel);

		initWidget(mainPanel);
	}

	// ===== LIST METHODS =====

	public void setTodoLists(List<TodoList> lists) {
		listsContainer.clear();
		listItemWidgets.clear();
		
		for (final TodoList list : lists) {
			Label listLabel = new Label(list.getName());
			listLabel.setStyleName("list-item");
			listLabel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectList(list.getId());
				}
			});
			
			listsContainer.add(listLabel);
			listItemWidgets.put(list.getId(), listLabel);
		}
	}

	public void addTodoList(TodoList list) {
		Label listLabel = new Label(list.getName());
		listLabel.setStyleName("list-item");
		final long listId = list.getId();
		listLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectList(listId);
			}
		});
		
		listsContainer.add(listLabel);
		listItemWidgets.put(list.getId(), listLabel);
	}

	public void removeTodoList(long id) {
		Label listLabel = listItemWidgets.get(id);
		if (listLabel != null) {
			listsContainer.remove(listLabel);
			listItemWidgets.remove(id);
		}
	}

	private void selectList(long id) {
		// Clear previous selection styling
		for (Label label : listItemWidgets.values()) {
			label.removeStyleName("list-item-selected");
		}
		
		// Select new list
		Label selected = listItemWidgets.get(id);
		if (selected != null) {
			selected.addStyleName("list-item-selected");
		}
		
		selectedListId = id;
		
		// Fire selection handler
		if (listSelectionHandler != null) {
			listSelectionHandler.onChange(null);
		}
	}

	public long getSelectedListId() {
		return selectedListId;
	}

	public String getNewListName() {
		return newListField.getText().trim();
	}

	public void clearNewListField() {
		newListField.setText("");
	}

	public void addTodoListSelectionHandler(ChangeHandler handler) {
		listSelectionHandler = handler;
	}

	public void addAddListButtonClickHandler(ClickHandler handler) {
		addListButton.addClickHandler(handler);
	}

	// ===== ITEM METHODS (Kept for compatibility, but moved to TodoDetailView) =====

	public void setListName(String name) {
		// Moved to TodoDetailView
	}

	public void setTodoItems(List<TodoItem> items) {
		// Items are now handled by TodoDetailView
	}

	public void addTodoItem(TodoItem item) {
		// Items are now handled by TodoDetailView
	}

	public void removeTodoItem(long id) {
		// Items are now handled by TodoDetailView
	}

	public void updateTodoItem(TodoItem item) {
		// Items are now handled by TodoDetailView
	}

	public long getSelectedItemId() {
		// Item selection will be handled differently
		return -1;
	}

	public String getNewItemTitle() {
		return "";
	}

	public void clearNewItemField() {
		// No-op
	}

	public void addTodoItemSelectionHandler(ChangeHandler handler) {
		// Item selection will be handled differently
	}

	public void addAddItemButtonClickHandler(ClickHandler handler) {
		// Add item will use a dialog/popup
	}

	public void showMessage(String message) {
		// no-op: messages are handled in the right panel
	}

	public void clearMessage() {
		// no-op
	}

	public void clear() {
		listsContainer.clear();
		listItemWidgets.clear();
		newListField.setText("");
		selectedListId = -1;
	}

	public void setItemsEnabled(boolean enabled) {
		// Items section is now handled separately
	}
}
