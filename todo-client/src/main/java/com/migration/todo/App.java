package com.migration.todo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.migration.todo.client.presenter.MainPresenter;
import com.migration.todo.client.ui.MainView;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Create the main view with MVP pattern
		MainView mainView = new MainView();
		
		// Create the presenter which handles all interactions
		MainPresenter presenter = new MainPresenter(mainView);

		// Add the main view to the root panel
		RootPanel.get().add(presenter.getView());
	}
}
