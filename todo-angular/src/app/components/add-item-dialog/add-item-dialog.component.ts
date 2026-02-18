import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TodoItemService } from '../../services/todo-item.service';
import { TodoItem } from '../../models/todo-item.model';

/**
 * Modal Dialog Component for creating new todo items
 * Replaces the GWT AddItemDialog
 */
@Component({
  selector: 'app-add-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-item-dialog.component.html',
  styleUrls: ['./add-item-dialog.component.css']
})
export class AddItemDialogComponent {
  @Input() visible: boolean = false;
  @Input() listId: number = -1;
  @Output() itemCreated = new EventEmitter<TodoItem>();
  @Output() closed = new EventEmitter<void>();

  title: string = '';
  description: string = '';
  loading: boolean = false;
  error: string = '';

  constructor(private todoItemService: TodoItemService) {}

  /**
   * Create a new item and close dialog
   */
  createItem(): void {
    if (!this.title.trim()) {
      this.error = 'Title is required';
      return;
    }

    if (this.listId <= 0) {
      this.error = 'No list selected';
      return;
    }

    this.loading = true;
    this.error = '';
    this.todoItemService.createItem(this.listId, this.title, this.description).subscribe({
      next: (newItem) => {
        this.itemCreated.emit(newItem);
        this.resetForm();
        this.close();
      },
      error: (err) => {
        this.error = 'Failed to create item';
        console.error('Error creating item:', err);
        this.loading = false;
      }
    });
  }

  /**
   * Close dialog without creating item
   */
  close(): void {
    this.resetForm();
    this.closed.emit();
  }

  /**
   * Reset form fields
   */
  private resetForm(): void {
    this.title = '';
    this.description = '';
    this.error = '';
    this.loading = false;
  }

  /**
   * Handle keyboard events
   */
  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.close();
    }
  }
}
