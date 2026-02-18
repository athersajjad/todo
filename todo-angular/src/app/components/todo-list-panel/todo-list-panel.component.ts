import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TodoListService } from '../../services/todo-list.service';
import { TodoList } from '../../models/todo-list.model';

/**
 * Left Panel Component - Displays scrollable list of todo lists
 * User can create new lists and select lists to view items
 */
@Component({
  selector: 'app-todo-list-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './todo-list-panel.component.html',
  styleUrls: ['./todo-list-panel.component.css']
})
export class TodoListPanelComponent implements OnInit {
  todoLists: TodoList[] = [];
  selectedListId: number = -1;
  newListName: string = '';
  loading: boolean = false;
  error: string = '';

  @Output() listSelected = new EventEmitter<number>();
  @Output() listNameChanged = new EventEmitter<string>();
  @Output() listCreated = new EventEmitter<void>();

  constructor(
    private todoListService: TodoListService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadLists();
  }

  /**
   * Load all todo lists from backend
   */
  loadLists(): void {
    console.log('Loading todo lists...');
    this.loading = true;
    this.error = '';
    this.todoListService.getAllLists().subscribe({
      next: (lists) => {
        console.log('Lists loaded successfully:', lists);
        this.todoLists = lists;
        if (lists.length > 0 && this.selectedListId === -1) {
          console.log('Auto-selecting first list:', lists[0].id);
          this.selectList(lists[0].id);
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = 'Failed to load lists: ' + (err.message || err.status || 'Unknown error');
        console.error('Error loading lists:', err);
        console.error('Error details:', {
          status: err.status,
          statusText: err.statusText,
          message: err.message,
          error: err.error,
          url: err.url
        });
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  /**
   * Create a new todo list
   */
  addList(): void {
    if (!this.newListName.trim()) {
      this.error = 'List name cannot be empty';
      return;
    }

    this.loading = true;
    this.error = '';
    this.todoListService.createList(this.newListName).subscribe({
      next: (newList) => {
        this.todoLists.push(newList);
        this.newListName = '';
        this.selectList(newList.id);
        this.listCreated.emit();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to create list';
        console.error('Error creating list:', err);
        this.loading = false;
      }
    });
  }

  /**
   * Select a todo list and emit event
   */
  selectList(id: number): void {
    console.log('Selecting list:', id);
    this.selectedListId = id;
    const selectedList = this.todoLists.find(list => list.id === id);
    if (selectedList) {
      console.log('Emitting list name:', selectedList.name);
      this.listNameChanged.emit(selectedList.name);
    }
    console.log('Emitting listSelected event:', id);
    this.listSelected.emit(id);
  }
}
