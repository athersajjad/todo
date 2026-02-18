/**
 * Represents a single todo item
 */
export interface TodoItem {
  id: number;
  listId: number;
  title: string;
  description: string;
  completed: boolean;
}

/**
 * DTO for creating a new TodoItem
 */
export class CreateItemRequest {
  constructor(
    public listId: number,
    public title: string,
    public description: string = '',
    public completed: boolean = false
  ) {}
}

/**
 * DTO for updating a TodoItem
 */
export class UpdateItemRequest {
  constructor(
    public title: string,
    public description: string,
    public completed: boolean
  ) {}
}
