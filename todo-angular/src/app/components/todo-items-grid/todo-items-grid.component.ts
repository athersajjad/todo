import { Component, OnInit, OnChanges, SimpleChanges, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TodoItemService } from '../../services/todo-item.service';
import { TodoItem } from '../../models/todo-item.model';

/**
 * Right Panel Component - Displays items from selected list in grid format
 * User can create, edit, delete items and toggle completion status
 */
@Component({
  selector: 'app-todo-items-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './todo-items-grid.component.html',
  styleUrls: ['./todo-items-grid.component.css']
})
export class TodoItemsGridComponent implements OnInit, OnChanges {
  @Input() selectedListId: number = -1;
  @Input() selectedListName: string = '';
  @Output() addItemClicked = new EventEmitter<void>();

  items: TodoItem[] = [];
  loading: boolean = false;
  error: string = '';

  constructor(
    private todoItemService: TodoItemService,
    private cdr: ChangeDetectorRef
  ) {
    console.log('[TodoItemsGrid] Constructor - selectedListId:', this.selectedListId);
  }

  ngOnInit(): void {
    console.log('[TodoItemsGrid] ngOnInit - selectedListId:', this.selectedListId);
    // Initial load if list is selected
    if (this.selectedListId > 0) {
      console.log('[TodoItemsGrid] Loading items in ngOnInit');
      this.loadItems();
    } else {
      console.log('[TodoItemsGrid] NOT loading items in ngOnInit (selectedListId <= 0)');
    }
  }

  /**
   * Watch for changes to selectedListId
   */
  ngOnChanges(changes: SimpleChanges): void {
    console.log('[TodoItemsGrid] ngOnChanges called');
    console.log('[TodoItemsGrid] changes:', changes);
    console.log('[TodoItemsGrid] selectedListId:', this.selectedListId);
    console.log('[TodoItemsGrid] selectedListName:', this.selectedListName);

    if (changes['selectedListId']) {
      console.log('[TodoItemsGrid] selectedListId changed from', changes['selectedListId'].previousValue, 'to', changes['selectedListId'].currentValue);
      if (this.selectedListId > 0) {
        console.log('[TodoItemsGrid] Calling loadItems()');
        this.loadItems();
      } else {
        console.log('[TodoItemsGrid] selectedListId <= 0, clearing items');
        this.items = [];
        this.error = '';
      }
    } else {
      console.log('[TodoItemsGrid] selectedListId did NOT change');
    }
  }

  /**
   * Load items for the selected list
   */
  loadItems(): void {
    if (this.selectedListId <= 0) return;

    console.log('Loading items for list ID:', this.selectedListId);
    this.loading = true;
    this.error = '';
    this.todoItemService.getItemsByListId(this.selectedListId).subscribe({
      next: (items) => {
        console.log('Items loaded successfully:', items);
        this.items = items;
        this.loading = false;
        console.log('[TodoItemsGrid] Triggering change detection');
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = 'Failed to load items: ' + (err.message || err.status || 'Unknown error');
        console.error('Error loading items:', err);
        console.error('Error details:', {
          status: err.status,
          statusText: err.statusText,
          message: err.message,
          error: err.error,
          url: err.url
        });
        this.loading = false;
        this.cdr.detectChanges();
      },
      complete: () => {
        console.log('Items observable completed');
      }
    });
  }

  /**
   * Emit event to open add item dialog
   */
  onAddItem(): void {
    this.addItemClicked.emit();
  }

  /**
   * Toggle completion status of an item
   */
  toggleItem(item: TodoItem): void {
    this.todoItemService.toggleItemCompletion(item.id).subscribe({
      next: (updatedItem) => {
        const index = this.items.findIndex(i => i.id === item.id);
        if (index !== -1) {
          this.items[index] = updatedItem;
        }
      },
      error: (err) => {
        console.error('Error toggling item:', err);
      }
    });
  }
}
