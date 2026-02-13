package com.migration.todo.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.migration.todo.TodoItem;

/**
 * View for displaying a list of todo items in a grid/table on the right panel.
 */
public class TodoItemsGridView extends Composite {

	private final FlowPanel mainPanel;
	private final FlowPanel headerPanel;  // contains title and add button
	private final Label titleLabel;
	private final FlexTable itemsGrid;
	private final Button addItemButton;
	private final Label messageLabel;
	
	private final Map<Long, Integer> itemRowMap; // Maps item ID to grid row number
	private long selectedItemId = -1;
	private ClickHandler itemSelectionHandler;
	private ItemCompletionHandler itemCompletionHandler;

	public interface ItemCompletionHandler {
		void onCompletionChanged(TodoItem item, boolean completed);
	}

	public TodoItemsGridView() {
		mainPanel = new FlowPanel();
		mainPanel.setStyleName("items-grid-view");
		mainPanel.setWidth("100%");

		// Header row: title on left, add button on right
		headerPanel = new FlowPanel();
		headerPanel.setStyleName("header-row");
		
		titleLabel = new Label("Select a list to view items");
		titleLabel.setStyleName("header-title");
		
		addItemButton = new Button("Add Item");
		addItemButton.setStyleName("todo-button");
		addItemButton.setEnabled(false);
		
		headerPanel.add(titleLabel);
		headerPanel.add(addItemButton);

		// Items Grid
		itemsGrid = new FlexTable();
		itemsGrid.setStyleName("items-grid");
		itemsGrid.setCellSpacing(0);
		itemsGrid.setCellPadding(0);

		// Grid Headers
		itemsGrid.setText(0, 0, "Status");
		itemsGrid.setText(0, 1, "Title");
		itemsGrid.setText(0, 2, "Description");
		itemsGrid.getRowFormatter().setStyleName(0, "grid-header");
		itemsGrid.getFlexCellFormatter().setWidth(0, 0, "50px");
		itemsGrid.getFlexCellFormatter().setWidth(0, 1, "150px");
		itemsGrid.getFlexCellFormatter().setWidth(0, 2, "250px");

		// Message label
		messageLabel = new Label("");
		messageLabel.setStyleName("todo-message");

		// Assemble - header row, grid (fills remaining space), message
		mainPanel.add(headerPanel);
		mainPanel.add(itemsGrid);
		mainPanel.add(messageLabel);

		itemRowMap = new HashMap<>();

		initWidget(mainPanel);
	}

	/**
	 * Set the title and display items for a todo list
	 */
	public void setListName(String name) {
		titleLabel.setText("Items in: " + name);
	}

	/**
	 * Populate the grid with items from a list
	 */
	public void setTodoItems(List<TodoItem> items) {
		// Clear grid except header
		while (itemsGrid.getRowCount() > 1) {
			itemsGrid.removeRow(1);
		}
		itemRowMap.clear();
		selectedItemId = -1;
		addItemButton.setEnabled(true);

		if (items == null || items.isEmpty()) {
			messageLabel.setText("No items in this list");
			return;
		}

		messageLabel.setText("");

		// Add items to grid
		int row = 1;
		for (final TodoItem item : items) {
			CheckBox statusCheckBox = new CheckBox();
			statusCheckBox.setValue(item.isCompleted());
			statusCheckBox.setStyleName("grid-cell clickable");
			statusCheckBox.getElement().getStyle().setProperty("accentColor", "green");
			statusCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (itemCompletionHandler != null) {
						itemCompletionHandler.onCompletionChanged(item, event.getValue());
					}
				}
			});
			Label titleLabel = new Label(item.getTitle());
			titleLabel.setStyleName("grid-cell clickable");
			String description = item.getDescription();
			if (description == null || description.trim().isEmpty()) {
				description = "NA";
			}
			Label descLabel = new Label(description);
			descLabel.setStyleName("grid-cell clickable");
			
			final int itemRow = row;
			final long itemId = item.getId();
			
			// Add click handler to all three cells
			ClickHandler rowClickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectItem(itemId, itemRow);
				}
			};
			
			// statusCheckBox.addClickHandler(rowClickHandler); // CheckBox handles its own clicks
			titleLabel.addClickHandler(rowClickHandler);
			descLabel.addClickHandler(rowClickHandler);

			itemsGrid.setWidget(row, 0, statusCheckBox);
			itemsGrid.setWidget(row, 1, titleLabel);
			itemsGrid.setWidget(row, 2, descLabel);
			
			itemsGrid.getRowFormatter().setStyleName(row, "grid-row");

			itemRowMap.put(itemId, row);
			row++;
		}
	}

	/**
	 * Add a new item to the grid
	 */
	public void addTodoItem(TodoItem item) {
		int row = itemsGrid.getRowCount();
		CheckBox statusCheckBox = new CheckBox();
		statusCheckBox.setValue(item.isCompleted());
		statusCheckBox.setStyleName("grid-cell clickable");
		statusCheckBox.getElement().getStyle().setProperty("accentColor", "green");
		statusCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (itemCompletionHandler != null) {
					itemCompletionHandler.onCompletionChanged(item, event.getValue());
				}
			}
		});
		Label titleLabel = new Label(item.getTitle());
		titleLabel.setStyleName("grid-cell clickable");
		String description = item.getDescription();
		if (description == null || description.trim().isEmpty()) {
			description = "NA";
		}
		Label descLabel = new Label(description);
		descLabel.setStyleName("grid-cell clickable");
		
		final int itemRow = row;
		final long itemId = item.getId();
		
		// Add click handler to all three cells
		ClickHandler rowClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectItem(itemId, itemRow);
			}
		};
		
		titleLabel.addClickHandler(rowClickHandler);
		descLabel.addClickHandler(rowClickHandler);

		itemsGrid.setWidget(row, 0, statusCheckBox);
		itemsGrid.setWidget(row, 1, titleLabel);
		itemsGrid.setWidget(row, 2, descLabel);
		
		itemsGrid.getRowFormatter().setStyleName(row, "grid-row");

		itemRowMap.put(itemId, row);
		messageLabel.setText("");
	}

	/**
	 * Update an item in the grid
	 */
	public void updateTodoItem(TodoItem item) {
		Integer row = itemRowMap.get(item.getId());
		if (row != null) {
			CheckBox statusCheckBox = new CheckBox();
			statusCheckBox.setValue(item.isCompleted());
			statusCheckBox.setStyleName("grid-cell clickable");
			statusCheckBox.getElement().getStyle().setProperty("accentColor", "green");
			statusCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (itemCompletionHandler != null) {
						itemCompletionHandler.onCompletionChanged(item, event.getValue());
					}
				}
			});
			Label titleLabel = new Label(item.getTitle());
			titleLabel.setStyleName("grid-cell clickable");
			String description = item.getDescription();
			if (description == null || description.trim().isEmpty()) {
				description = "NA";
			}
			Label descLabel = new Label(description);
			descLabel.setStyleName("grid-cell clickable");
			
			final int itemRow = row;
			final long itemId = item.getId();
			
			// Add click handler to all three cells
			ClickHandler rowClickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectItem(itemId, itemRow);
				}
			};
			
			titleLabel.addClickHandler(rowClickHandler);
			descLabel.addClickHandler(rowClickHandler);

			itemsGrid.setWidget(row, 0, statusCheckBox);
			itemsGrid.setWidget(row, 1, titleLabel);
			itemsGrid.setWidget(row, 2, descLabel);
		}
	}

	/**
	 * Remove an item from the grid
	 */
	public void removeTodoItem(long id) {
		Integer row = itemRowMap.get(id);
		if (row != null) {
			itemsGrid.removeRow((int) row);
			itemRowMap.remove(id);
			
			// Update row mappings for items after the removed one
			for (Long itemId : itemRowMap.keySet()) {
				int currentRow = itemRowMap.get(itemId);
				if (currentRow > row) {
					itemRowMap.put(itemId, currentRow - 1);
				}
			}
			
			if (itemsGrid.getRowCount() <= 1) {
				messageLabel.setText("No items in this list");
			}
		}
	}

	/**
	 * Select an item by clicking on it
	 */
	private void selectItem(long itemId, int row) {
		// Remove previous selection styling
		for (Integer prevRow : itemRowMap.values()) {
			itemsGrid.getRowFormatter().removeStyleName(prevRow, "grid-row-selected");
		}

		// Apply selection styling
		selectedItemId = itemId;
		itemsGrid.getRowFormatter().addStyleName(row, "grid-row-selected");

		// Fire selection handler
		if (itemSelectionHandler != null) {
			itemSelectionHandler.onClick(null);
		}
	}

	public long getSelectedItemId() {
		return selectedItemId;
	}

	public void addItemSelectionHandler(ClickHandler handler) {
		this.itemSelectionHandler = handler;
	}

	public void addItemCompletionHandler(ItemCompletionHandler handler) {
		this.itemCompletionHandler = handler;
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
		while (itemsGrid.getRowCount() > 1) {
			itemsGrid.removeRow(1);
		}
		itemRowMap.clear();
		selectedItemId = -1;
		titleLabel.setText("Select a list to view items");
		messageLabel.setText("");
		addItemButton.setEnabled(false);
	}

	public void setEnabled(boolean enabled) {
		addItemButton.setEnabled(enabled);
	}
}
