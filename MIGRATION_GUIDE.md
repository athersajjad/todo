# GWT to Angular Migration Guide

## Overview
This document outlines the migration from GWT RPC to Angular + Spring Web MVC REST API.

## Completed Steps

### 1. Spring Web MVC REST Controllers ✅
- **TodoListController** (`/api/lists`): GET all lists, POST create, DELETE by ID
- **TodoItemController** (`/api/items`): GET by list, GET by ID, POST create, PUT update, DELETE, PUT toggle completion
- **CORS Configuration**: Enabled for Angular frontend (localhost:4200, localhost:3000)
- **JSON Serialization**: Jackson configured for automatic model-to-JSON mapping

### 2. External DTOs ✅
- `CreateListRequest`: { name: String }
- `UpdateItemRequest`: { title, description, completed }
- `CreateItemRequest` (in TodoItemController): { listId, title, description, completed }

### 3. Spring Configuration ✅
- `spring-servlet.xml`: Component scanning and MVC annotation support
- `web.xml`: DispatcherServlet mapped to `/api/*`, preserving GWT RPC at legacy paths
- `WebConfig.java`: CORS configuration for cross-origin requests

### 4. Dependencies ✅
- Spring Framework 5.3.30 (no Spring Boot)
- Jackson 2.15.2 for JSON handling
- All Maven dependencies configured

## REST API Endpoints

### TodoList Endpoints

```
GET  /api/lists                  → List<TodoList>
POST /api/lists                  → TodoList (body: CreateListRequest)
DELETE /api/lists/{id}           → 204 No Content | 404 Not Found
```

### TodoItem Endpoints

```
GET    /api/items/list/{listId}  → List<TodoItem>
GET    /api/items/{id}           → TodoItem
POST   /api/items                → TodoItem (body: CreateItemRequest)
PUT    /api/items/{id}           → TodoItem (body: UpdateItemRequest)
DELETE /api/items/{id}           → 204 No Content
PUT    /api/items/{id}/toggle    → TodoItem (toggle completion)
```

## Next Steps: Angular Frontend Setup

### 1. Create Angular Project
```bash
ng new todo-app
cd todo-app
ng add @angular/material  # Optional for UI components
```

### 2. Required Angular Structure
```
src/
├── app/
│   ├── models/
│   │   ├── todo-list.model.ts
│   │   └── todo-item.model.ts
│   ├── services/
│   │   ├── todo-list.service.ts
│   │   └── todo-item.service.ts
│   ├── components/
│   │   ├── main/
│   │   ├── todo-list-panel/
│   │   ├── todo-items-grid/
│   │   ├── add-item-dialog/
│   │   └── add-list-dialog/
│   └── app.component.ts
├── styles/
│   └── todo.css  # Reuse flex CSS from GWT
└── environments/
    └── environment.ts
```

### 3. TypeScript Models
```typescript
// todo-list.model.ts
export interface TodoList {
  id: number;
  name: string;
}

// todo-item.model.ts
export interface TodoItem {
  id: number;
  listId: number;
  title: string;
  description: string;
  completed: boolean;
}
```

### 4. Services Structure
```typescript
// todo-list.service.ts
@Injectable({ providedIn: 'root' })
export class TodoListService {
  constructor(private http: HttpClient) { }
  
  getAllLists(): Observable<TodoList[]> { ... }
  createList(name: string): Observable<TodoList> { ... }
  deleteList(id: number): Observable<void> { ... }
}

// todo-item.service.ts
@Injectable({ providedIn: 'root' })
export class TodoItemService {
  constructor(private http: HttpClient) { }
  
  getItemsByListId(listId: number): Observable<TodoItem[]> { ... }
  createItem(listId: number, title: string, description: string): Observable<TodoItem> { ... }
  updateItem(id: number, item: Partial<TodoItem>): Observable<TodoItem> { ... }
  deleteItem(id: number): Observable<void> { ... }
  toggleItem(id: number): Observable<TodoItem> { ... }
}
```

### 5. Component Structure (Replaces GWT Views)

**MainComponent** (replaces MainView)
- 2-panel flex layout (left: 220px, right: flex-1)
- Left content: TodoListPanelComponent
- Right content: TodoItemsGridComponent

**TodoListPanelComponent** (replaces TodoListView)
- Displays scrollable list of todo lists
- Add list form at top
- Click handler for list selection

**TodoItemsGridComponent** (replaces TodoItemsGridView)
- Header: list name + add button (space-between)
- Grid: displays items
- Click handler for item selection

**AddItemDialogComponent** (replaces AddItemDialog)
- Modal dialog for creating items
- Title + description fields
- Submit callback

### 6. CSS Reuse
The existing `todo.css` flexbox styling is fully compatible with Angular:
```css
.main-container { display:flex; height:100vh; flex-direction:row; gap:16px; }
.left-panel { flex:0 0 220px; width:220px; display:flex; flex-direction:column; }
.right-panel { flex:1 1 auto; }
/* ... etc ... */
```

## Running the Application

### Backend (Spring)
```bash
cd todo-server
mvn clean compile
mvn jetty:run
# Server runs on http://localhost:8080
# REST API available at http://localhost:8080/api/*
```

### Frontend (Angular)
```bash
cd todo-app
ng serve
# App runs on http://localhost:4200
```

## API Response Examples

### GET /api/lists
```json
[
  { "id": 1, "name": "My Tasks" },
  { "id": 2, "name": "Shopping" }
]
```

### POST /api/items with CreateItemRequest
```json
{
  "listId": 1,
  "title": "Finish migration",
  "description": "Complete Angular setup",
  "completed": false
}
```

### Response
```json
{
  "id": 101,
  "listId": 1,
  "title": "Finish migration",
  "description": "Complete Angular setup",
  "completed": false
}
```

## Key Differences from GWT

| Feature | GWT | Angular |
|---------|-----|---------|
| Network | RPC binary (GWT RPC) | REST JSON (HttpClient) |
| Serialization | GWT automatic | Jackson (server), HttpClient (client) |
| Views | GWT Widgets | Angular Components |
| Layout | FlowPanel + CSS flex | CSS flex (same) |
| Data Binding | Custom event handlers | @angular/forms two-way binding |
| Async | GWT AsyncCallback | RxJS Observable |
| HTTP | GWT RPC | Angular HttpClient |
| Dialog | GWT DialogBox | Angular Material Dialog |
| Styling | CSS Flexbox (reusable) | CSS Flexbox (reusable) |

## GWT RPC Methods (Mapped to REST)

```
getTodoListsByName()           → GET /api/lists (filtered client-side)
getAllTodoLists()              → GET /api/lists
createTodoList(name)           → POST /api/lists
deleteTodoList(id)             → DELETE /api/lists/{id}
getTodoItemsByListId(listId)   → GET /api/items/list/{listId}
getTodoById(id)                → GET /api/items/{id}
createTodo(...)                → POST /api/items
updateTodo(...)                → PUT /api/items/{id}
deleteTodo(id)                 → DELETE /api/items/{id}
toggleTodoCompletion(id)       → PUT /api/items/{id}/toggle
```

## Testing the REST API

Use curl to test endpoints:
```bash
# Get all lists
curl http://localhost:8080/api/lists

# Create a list
curl -X POST http://localhost:8080/api/lists \
  -H "Content-Type: application/json" \
  -d '{"name":"Test List"}'

# Get items for a list
curl http://localhost:8080/api/items/list/1

# Create an item
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{"listId":1,"title":"Test Item","description":"Test","completed":false}'
```

## Current Status
- ✅ Spring REST controllers created
- ✅ CORS configuration enabled
- ✅ Maven dependencies configured
- ⏳ Angular project scaffolding (next)
- ⏳ Angular services implementation (next)
- ⏳ Angular components creation (next)
- ⏳ Data binding and event handlers (next)

## Notes
- GWT RPC endpoints remain active for backward compatibility
- CSS flexbox styling is fully reusable in Angular
- Database and business logic (TodoServiceImpl) remain unchanged
- Spring Framework version: 5.3.30 (no Spring Boot)
- Angular version: 16+ (recommended)
