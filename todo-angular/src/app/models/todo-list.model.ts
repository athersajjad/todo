/**
 * Represents a todo list container
 */
export interface TodoList {
  id: number;
  name: string;
}

/**
 * DTO for creating a new TodoList
 */
export class CreateListRequest {
  constructor(public name: string) {}
}
