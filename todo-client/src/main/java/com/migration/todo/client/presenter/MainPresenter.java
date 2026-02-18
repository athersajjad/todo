package com.migration.todo.client.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.migration.todo.TodoItem;
import com.migration.todo.TodoList;
import com.migration.todo.TodoService;
import com.migration.todo.TodoServiceAsync;
import com.migration.todo.client.ui.AddItemDialog;
import com.migration.todo.client.ui.MainView;
import com.migration.todo.client.ui.TodoItemsGridView;
import com.migration.todo.client.ui.TodoListView;

/**
 * Main presenter for the todo application. Implements the MVP pattern
 * to handle all user interactions and business logic.
 */
public class MainPresenter {

	private final MainView view;
	private final TodoServiceAsync todoService;
	private final AddItemDialog addItemDialog;
	private List<TodoList> allLists;
	private List<TodoItem> currentListItems;
	private long selectedListId = -1;

	public MainPresenter(MainView view) {
		this.view = view;
		this.todoService = GWT.create(TodoService.class);
		this.addItemDialog = new AddItemDialog();

		bindViewHandlers();
		loadTodoLists();
	}

	/**
	 * Bind event handlers to view elements.
	 */
	private void bindViewHandlers() {
		TodoListView listView = view.getTodoListView();
		TodoItemsGridView itemsGridView = view.getTodoItemsGridView();

		// Handle todo list selection
		listView.addTodoListSelectionHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				onListSelected();
			}
		});

		// Handle add list button click
		listView.addAddListButtonClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAddListClicked();
			}
		});

		// Handle add item button click
		itemsGridView.addAddItemButtonClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAddItemClicked();
			}
		});

		// Handle item selection from the grid
		itemsGridView.addItemSelectionHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onItemSelected();
			}
		});

		// Handle item completion changes
		itemsGridView.addItemCompletionHandler(new TodoItemsGridView.ItemCompletionHandler() {
			@Override
			public void onCompletionChanged(TodoItem item, boolean completed) {
				updateItemCompletion(item, completed);
			}
		});
	}

	/**
	 * Load all todo lists from the server.
	 */
	private void loadTodoLists() {
		view.getTodoListView().showMessage("Loading...");
		view.getTodoListView().setItemsEnabled(false);
		view.getTodoItemsGridView().setEnabled(false);

		todoService.getAllTodoLists(new AsyncCallback<List<TodoList>>() {
			@Override
			public void onSuccess(List<TodoList> result) {
				allLists = result;
				view.getTodoListView().setTodoLists(result);
				view.getTodoListView().clearMessage();

				if (!result.isEmpty()) {
					view.getTodoListView().setItemsEnabled(true);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				view.getTodoListView().showMessage("Error loading lists");
				System.err.println("Error loading lists: " + caught.getMessage());
				caught.printStackTrace();
			}
		});
	}

	/**
	 * Load items for the selected todo list.
	 */
	private void loadListItems(long listId) {
		view.getTodoItemsGridView().showMessage("Loading items...");
		view.getTodoItemsGridView().setEnabled(false);

		todoService.getTodoItemsByListId(listId, new AsyncCallback<List<TodoItem>>() {
			@Override
			public void onSuccess(List<TodoItem> result) {
				currentListItems = result;
				view.getTodoItemsGridView().setTodoItems(result);
				view.getTodoItemsGridView().clearMessage();
				view.getTodoItemsGridView().setEnabled(true);
			}

			@Override
			public void onFailure(Throwable caught) {
				view.getTodoItemsGridView().showMessage("Error loading items");
				System.err.println("Error loading items: " + caught.getMessage());
				caught.printStackTrace();
			}
		});
	}

	/**
	 * Handle list selection from the list view.
	 */
	private void onListSelected() {
		TodoListView listView = view.getTodoListView();
		long selectedId = listView.getSelectedListId();

		if (selectedId > 0) {
			selectedListId = selectedId;

			// Find the list name
			TodoList selectedList = findListById(selectedId);
			if (selectedList != null) {
				view.getTodoItemsGridView().setListName(selectedList.getName());
				loadListItems(selectedId);
			}
		}
	}

	/**
	 * Handle add list button click.
	 */
	private void onAddListClicked() {
		TodoListView listView = view.getTodoListView();
		String name = listView.getNewListName();

		if (name.isEmpty()) {
			listView.showMessage("Please enter a list name");
			return;
		}

		listView.showMessage("Adding list...");

		todoService.createTodoList(name, new AsyncCallback<TodoList>() {
			@Override
			public void onSuccess(TodoList result) {
				allLists.add(result);
				view.getTodoListView().addTodoList(result);
				view.getTodoListView().clearNewListField();
				view.getTodoListView().clearMessage();
			}

			@Override
			public void onFailure(Throwable caught) {
				view.getTodoListView().showMessage("Error adding list");
				System.err.println("Error adding list: " + caught.getMessage());
				caught.printStackTrace();
			}
		});
	}



	/**
	 * Handle item selection from the grid.
	 */
	private void onItemSelected() {
		TodoItemsGridView gridView = view.getTodoItemsGridView();
		long selectedId = gridView.getSelectedItemId();

		if (selectedId > 0) {
			TodoItem selectedItem = findItemById(selectedId);
			if (selectedItem != null) {
				// Item selected in grid - could open detail view if needed
				gridView.showMessage("Selected: " + selectedItem.getTitle());
				new com.google.gwt.user.client.Timer() {
					@Override
					public void run() {
						gridView.clearMessage();
					}
				}.schedule(1000);
			}
		}
	}

	/**
	 * Handle add item button click.
	 */
	private void onAddItemClicked() {
		if (selectedListId <= 0) {
			view.getTodoItemsGridView().showMessage("Please select a list first");
			return;
		}

		addItemDialog.show(selectedListId, new AddItemDialog.Callback() {
			@Override
			public void onSave(String title, String description, long listId) {
				createNewItem(title, description, listId);
			}

			@Override
			public void onCancel() {
				// Dialog closed without saving
			}
		});
	}

	/**
	 * Create a new item in the selected list.
	 */
	private void createNewItem(String title, String description, long listId) {
		TodoItemsGridView gridView = view.getTodoItemsGridView();
		gridView.showMessage("Adding item...");

		todoService.createTodo(listId, title, description, false, new AsyncCallback<TodoItem>() {
			@Override
			public void onSuccess(TodoItem result) {
				currentListItems.add(result);
				gridView.addTodoItem(result);

				gridView.showMessage("Item added!");
				new com.google.gwt.user.client.Timer() {
					@Override
					public void run() {
						gridView.clearMessage();
					}
				}.schedule(1500);
			}

			@Override
			public void onFailure(Throwable caught) {
				gridView.showMessage("Error adding item");
				System.err.println("Error adding item: " + caught.getMessage());
				caught.printStackTrace();
			}
		});
	}

	/**
	 * Update item completion status.
	 */
	private void updateItemCompletion(final TodoItem item, final boolean completed) {
		// Optimistic update
		item.setCompleted(completed);

		todoService.updateTodo(item.getId(), item.getTitle(), item.getDescription(), completed, new AsyncCallback<TodoItem>() {
			@Override
			public void onSuccess(TodoItem result) {
				// Success, model is already updated
			}

			@Override
			public void onFailure(Throwable caught) {
				// Revert on failure
				item.setCompleted(!completed);
				view.getTodoItemsGridView().updateTodoItem(item);
				view.getTodoItemsGridView().showMessage("Error updating item status");
			}
		});
	}

	/**
	 * Find a list by its ID.
	 */
	private TodoList findListById(long id) {
		if (allLists != null) {
			for (TodoList list : allLists) {
				if (list.getId() == id) {
					return list;
				}
			}
		}
		return null;
	}

	/**
	 * Find an item by its ID in the current list.
	 */
	private TodoItem findItemById(long id) {
		if (currentListItems != null) {
			for (TodoItem item : currentListItems) {
				if (item.getId() == id) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Get the main view.
	 */
	public MainView getView() {
		return view;
	}
}
