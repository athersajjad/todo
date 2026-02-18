import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TodoItem, CreateItemRequest, UpdateItemRequest } from '../models/todo-item.model';

/**
 * Service for TodoItem API operations
 * Communicates with REST endpoints: /api/items
 */
@Injectable({
  providedIn: 'root'
})
export class TodoItemService {
  private apiUrl = 'http://localhost:8080/api/items';

  constructor(private http: HttpClient) {}

  /**
   * Get all items for a specific list
   */
  getItemsByListId(listId: number): Observable<TodoItem[]> {
    return this.http.get<TodoItem[]>(`${this.apiUrl}/list/${listId}`);
  }

  /**
   * Get a specific item by ID
   */
  getItemById(id: number): Observable<TodoItem> {
    return this.http.get<TodoItem>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create a new todo item
   */
  createItem(listId: number, title: string, description: string = ''): Observable<TodoItem> {
    const request = new CreateItemRequest(listId, title, description, false);
    return this.http.post<TodoItem>(this.apiUrl, request);
  }

  /**
   * Update an existing todo item
   */
  updateItem(id: number, title: string, description: string, completed: boolean): Observable<TodoItem> {
    const request = new UpdateItemRequest(title, description, completed);
    return this.http.put<TodoItem>(`${this.apiUrl}/${id}`, request);
  }

  /**
   * Toggle the completion status of a todo item
   */
  toggleItemCompletion(id: number): Observable<TodoItem> {
    return this.http.put<TodoItem>(`${this.apiUrl}/${id}/toggle`, {});
  }
}
