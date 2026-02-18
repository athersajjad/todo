# Todo Angular App

This is the Angular frontend for the Todo application, migrated from GWT.

## Architecture

### 2-Panel Layout
- **Left Panel (220px fixed)**: Todo Lists panel with add/delete functionality
- **Right Panel (flexible)**: Todo Items grid with add/delete/toggle functionality
- **Dialog**: Modal for creating new items

### Components

#### TodoListPanelComponent
- Displays scrollable list of todo lists
- Add new list form
- Delete list functionality
- List selection and highlight
- Emits list ID and name when selected

#### TodoItemsGridComponent
- Displays items from selected list
- Header row with list name and add button
- Grid of items with title, description, and completion checkbox
- Delete item functionality
- Toggle completion status
- Loading and error states

#### AddItemDialogComponent
- Modal dialog overlay for creating items
- Form fields: title (required), description (optional)
- Submit and cancel buttons
- Keyboard shortcuts (Enter to submit, Escape to close)
- Loading state management

### Services

#### TodoListService
REST API communication for:
- `GET /api/lists` - Get all lists
- `POST /api/lists` - Create new list
- `DELETE /api/lists/{id}` - Delete list

#### TodoItemService
REST API communication for:
- `GET /api/items/list/{listId}` - Get items by list
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item
- `PUT /api/items/{id}/toggle` - Toggle completion

### Models

#### TodoList
```typescript
interface TodoList {
  id: number;
  name: string;
}
```

#### TodoItem
```typescript
interface TodoItem {
  id: number;
  listId: number;
  title: string;
  description: string;
  completed: boolean;
}
```

## Development

### Prerequisites
- Node.js 18+
- npm 9+
- Angular CLI 17+

### Installation
```bash
cd todo-angular
npm install
```

### Running Development Server
```bash
npm start
# or
ng serve
```

Then navigate to `http://localhost:4200/`. The app will automatically reload when you modify any source files.

### Building for Production
```bash
npm run build
# or
ng build --configuration production
```

The build artifacts will be stored in the `dist/` directory.

## API Configuration

The application communicates with the backend REST API at:
```
http://localhost:8080/api
```

This is configured in the services:
- `src/app/services/todo-list.service.ts`
- `src/app/services/todo-item.service.ts`

To change the API URL, update the `apiUrl` property in both services.

## Styling

### Global Styles
- `src/styles.css` - Global resets and base styles

### Component Styles
Each component has its own CSS file using flexbox layout:
- `src/app/app.css` - Main 2-panel layout
- `src/app/components/todo-list-panel/todo-list-panel.component.css`
- `src/app/components/todo-items-grid/todo-items-grid.component.css`
- `src/app/components/add-item-dialog/add-item-dialog.component.css`

### Layout Approach
- All layouts use CSS Flexbox
- Left panel: `flex: 0 0 220px` (fixed 220px width)
- Right panel: `flex: 1 1 auto` (fills remaining space)
- Responsive design with scrollable areas

## Data Flow

1. **App Component**
   - Manages selectedListId and showAddItemDialog state
   - Listens for list selection from TodoListPanel
   - Passes selectedListId to TodoItemsGrid
   - Controls visibility of AddItemDialog

2. **TodoListPanel**
   - Loads all lists on init
   - Emits listSelected(id) and listNameChanged(name)
   - Handles create/delete operations

3. **TodoItemsGrid**
   - Watches for selectedListId changes
   - Loads items when list is selected
   - Emits addItemClicked(), itemDeleted()
   - Handles item operations

4. **AddItemDialog**
   - Opens when addItemClicked is emitted
   - On create, calls TodoItemService
   - Emits itemCreated(), closed()

## Error Handling

- API errors display inline error messages
- Loading states show spinners/messages
- Failed operations show user-friendly error text
- Console logs provide developer debugging info

## Performance

- Standalone components (no module needed)
- OnPush change detection ready
- Efficient CSS with flexbox (no table layout)
- Minimal re-renders with OnChanges
- Observable-based async operations

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+

## Migration Notes

This replaces the GWT client-side application:
- GWT Widgets → Angular Components
- GWT RPC → REST API with HttpClient
- GWT Events → Angular @Output EventEmitter
- FlowPanel/CSS → Pure CSS Flexbox
- Hidden labels → State variables

## Additional Commands

### Run Unit Tests
```bash
npm test
# or
ng test
```

### Run E2E Tests
```bash
npm run e2e
# or
ng e2e
```

### Lint Code
```bash
npm run lint
# or
ng lint
```

## Troubleshooting

### CORS Issues
If you see CORS errors, ensure:
1. Backend has CORS enabled for `http://localhost:4200`
2. Backend is running on `http://localhost:8080`
3. API endpoint is `http://localhost:8080/api`

### Backend Not Responding
```bash
# Backend should be running at localhost:8080
# Check Spring server logs for errors
# Verify REST endpoints exist (see MIGRATION_GUIDE.md)
```

### List Not Loading
- Check browser network tab in DevTools
- Verify TodoListService is calling correct API URL
- Check backend `/api/lists` endpoint

## File Structure

```
src/
├── app/
│   ├── components/
│   │   ├── add-item-dialog/
│   │   │   ├── add-item-dialog.component.ts
│   │   │   ├── add-item-dialog.component.html
│   │   │   └── add-item-dialog.component.css
│   │   ├── todo-items-grid/
│   │   │   ├── todo-items-grid.component.ts
│   │   │   ├── todo-items-grid.component.html
│   │   │   └── todo-items-grid.component.css
│   │   └── todo-list-panel/
│   │       ├── todo-list-panel.component.ts
│   │       ├── todo-list-panel.component.html
│   │       └── todo-list-panel.component.css
│   ├── models/
│   │   ├── todo-item.model.ts
│   │   └── todo-list.model.ts
│   ├── services/
│   │   ├── todo-item.service.ts
│   │   └── todo-list.service.ts
│   ├── app.ts (main component)
│   ├── app.html
│   └── app.css
├── index.html
├── main.ts
└── styles.css
```

## Next Steps

1. Start the Spring backend: `mvn jetty:run` in `todo-server`
2. Start the Angular dev server: `npm start` in `todo-angular`
3. Open `http://localhost:4200` in your browser
4. Create todo lists and items

## Support

For issues or questions about the migration, see `MIGRATION_GUIDE.md` in the project root.
