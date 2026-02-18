import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TodoList, CreateListRequest } from '../models/todo-list.model';

/**
 * Service for TodoList API operations
 * Communicates with REST endpoint: /api/lists
 */
@Injectable({
  providedIn: 'root'
})
export class TodoListService {
  private apiUrl = 'http://localhost:8080/api/lists';

  constructor(private http: HttpClient) {}

  /**
   * Get all todo lists
   */
  getAllLists(): Observable<TodoList[]> {
    return this.http.get<TodoList[]>(this.apiUrl);
  }

  /**
   * Create a new todo list
   */
  createList(name: string): Observable<TodoList> {
    const request = new CreateListRequest(name);
    return this.http.post<TodoList>(this.apiUrl, request);
  }
}
