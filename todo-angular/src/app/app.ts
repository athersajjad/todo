import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TodoListPanelComponent } from './components/todo-list-panel/todo-list-panel.component';
import { TodoItemsGridComponent } from './components/todo-items-grid/todo-items-grid.component';
import { AddItemDialogComponent } from './components/add-item-dialog/add-item-dialog.component';

/**
 * Main Application Component
 * Displays 2-panel layout: left panel for lists, right panel for items
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    TodoListPanelComponent,
    TodoItemsGridComponent,
    AddItemDialogComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  @ViewChild(TodoItemsGridComponent) itemsGrid!: TodoItemsGridComponent;

  selectedListId: number = -1;
  selectedListName: string = '';
  showAddItemDialog: boolean = false;

  constructor() {
    console.log('[App] Component initialized');
  }

  ngOnInit(): void {
    console.log('[App] ngOnInit - selectedListId:', this.selectedListId);
  }

  /**
   * Handle list selection
   */
  onListSelected(listId: number): void {
    console.log('[App] onListSelected called with:', listId);
    console.log('[App] Before assignment - selectedListId:', this.selectedListId);
    this.selectedListId = listId;
    console.log('[App] After assignment - selectedListId:', this.selectedListId);
  }

  /**
   * Update selected list name when changed
   */
  onListNameChanged(name: string): void {
    console.log('[App] onListNameChanged called with:', name);
    this.selectedListName = name;
  }

  /**
   * Open add item dialog
   */
  onAddItemClicked(): void {
    this.showAddItemDialog = true;
  }

  /**
   * Handle item created
   */
  onItemCreated(): void {
    this.showAddItemDialog = false;
    // Reload items to show the newly created item
    if (this.itemsGrid) {
      this.itemsGrid.loadItems();
    }
  }

  /**
   * Close add item dialog
   */
  onDialogClosed(): void {
    this.showAddItemDialog = false;
  }
}

