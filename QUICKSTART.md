# Todo App - Complete Setup & Running Guide

## Project Structure

```
/Users/athersajjad/Documents/migration/todo/
├── pom.xml                          (Parent Maven config)
├── MIGRATION_GUIDE.md               (Detailed migration documentation)
├── todo-server/                     (Spring Web MVC Backend)
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/migration/todo/
│   │   │   │   ├── rest/            (NEW REST Controllers)
│   │   │   │   │   ├── TodoListController.java
│   │   │   │   │   ├── TodoItemController.java
│   │   │   │   │   └── CreateListRequest.java
│   │   │   │   ├── config/          (NEW Spring Config)
│   │   │   │   │   └── WebConfig.java (CORS Configuration)
│   │   │   │   ├── TodoServiceImpl.java (existing)
│   │   │   │   └── (other existing classes)
│   │   │   └── resources/
│   │   │       └── spring-servlet.xml (NEW Spring config)
│   │   └── webapp/
│   │       └── WEB-INF/web.xml      (UPDATED with DispatcherServlet)
│   └── target/
├── todo-angular/                    (NEW Angular Frontend)
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── add-item-dialog/
│   │   │   │   ├── todo-items-grid/
│   │   │   │   └── todo-list-panel/
│   │   │   ├── models/
│   │   │   │   ├── todo-item.model.ts
│   │   │   │   └── todo-list.model.ts
│   │   │   ├── services/
│   │   │   │   ├── todo-item.service.ts
│   │   │   │   └── todo-list.service.ts
│   │   │   ├── app.ts
│   │   │   ├── app.html
│   │   │   └── app.css
│   │   ├── styles.css
│   │   └── main.ts
│   ├── package.json
│   └── README_ANGULAR.md
├── todo-client/                     (GWT Client - Being deprecated)
│   └── src/main/...
├── todo-server/                    (Existing)
└── todo-shared/                    (Existing)
```

## Backend Setup (Spring Web MVC)

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- SQLite JDBC driver (auto-installed by Maven)

### Installation & Running

```bash
# Navigate to project root
cd /Users/athersajjad/Documents/migration/todo

# Build entire project (including backend)
mvn clean install

# Run the backend server
cd todo-server
mvn jetty:run

# Expected output:
# Started ServerConnector@... LISTENING
# Server running on http://localhost:8080
```

### Test Backend API

```bash
# Get all lists
curl http://localhost:8080/api/lists

# Create a list
curl -X POST http://localhost:8080/api/lists \
  -H "Content-Type: application/json" \
  -d '{"name":"My First List"}'

# Expected response:
# {"id":1,"name":"My First List"}
```

## Frontend Setup (Angular)

### Prerequisites
- Node.js 18+ (includes npm)
- Angular CLI 17+

### Installation & Running

```bash
# Navigate to Angular project
cd /Users/athersajjad/Documents/migration/todo/todo-angular

# Install dependencies (already done, but if needed)
npm install

# Start development server
npm start
# or
ng serve

# Expected output:
# ✔ Application bundle generation complete. [X.XXX seconds]
# Watch mode enabled. File updates will trigger a rebuild.
# ㄱ Building...
# Local:   http://localhost:4200
```

Then open http://localhost:4200 in your browser.

## Complete Startup Procedure

### Terminal 1: Start Backend
```bash
cd /Users/athersajjad/Documents/migration/todo/todo-server
mvn jetty:run
# Wait for "Started ServerConnector@..." message
```

### Terminal 2: Start Angular Dev Server
```bash
cd /Users/athersajjad/Documents/migration/todo/todo-angular
npm start
# Wait for "Local: http://localhost:4200" message
```

### Terminal 3: Browser
```bash
# Open browser and navigate to
http://localhost:4200

# You should see:
# - Left panel: "Todo Lists" with add form and empty list
# - Right panel: "Select a list from the left to view items"
```

## Testing the Application

### Create a Todo List
1. In the left panel, enter a name in "New list name..." field
2. Click "Add" button
3. List should appear below with blue highlight (selected)

### Create a Todo Item
1. With a list selected, click "+ Add Item" button in right panel
2. Enter item title (required) and description (optional)
3. Click "Create Item"
4. Item should appear in the grid

### Manage Items
- **Toggle completion**: Click checkbox next to item
- **Delete item**: Hover over item, click "✕" button
- **Delete list**: Hover over list name, click "✕" button

## Troubleshooting

### Backend won't start
```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill if needed
kill -9 <PID>

# Try again
mvn jetty:run
```

### Angular dev server won't start
```bash
# Check if port 4200 is in use
lsof -i :4200

# Try different port
ng serve --port 4201
```

### CORS errors in browser
- Ensure backend is running on port 8080
- Check that WebConfig.java is properly configured
- Verify `/api/*` requests are going to Spring DispatcherServlet

### Items not loading
1. Check browser DevTools → Network tab
2. Look for requests to `http://localhost:8080/api/items/list/{id}`
3. Check response code and message
4. Check backend console for errors

### Database issues
- Database is stored at project root
- Delete the database file to reset: `todo.db`
- Backend will recreate it on next run with sample data

## Architecture Overview

```
Browser (Angular 16+) at localhost:4200
    ↓ HTTP (JSON)
    ↓ REST API requests
Spring Web MVC at localhost:8080
    ↓ Business Logic
TodoServiceImpl (existing)
    ↓ JDBC
SQLite Database (todo.db)
```

## Key Endpoints

### Lists
- `GET /api/lists` → List all todo lists
- `POST /api/lists` → Create new list
  ```json
  { "name": "Shopping" }
  ```
- `DELETE /api/lists/{id}` → Delete list

### Items
- `GET /api/items/list/{listId}` → Get items for list
- `GET /api/items/{id}` → Get specific item
- `POST /api/items` → Create item
  ```json
  { "listId": 1, "title": "Buy milk", "description": "2% milk", "completed": false }
  ```
- `PUT /api/items/{id}` → Update item
  ```json
  { "title": "Buy milk", "description": "2% milk", "completed": true }
  ```
- `DELETE /api/items/{id}` → Delete item
- `PUT /api/items/{id}/toggle` → Toggle completion status

## Configuration

### Backend API URL
- Located in Angular services: `src/app/services/`
- Change `apiUrl` in each service if backend is on different host/port

### CORS Settings
- Located in `src/main/java/com/migration/todo/config/WebConfig.java`
- Default allowed origins:
  - http://localhost:4200
  - http://localhost:3000
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS

### Server Port
- Backend: http://localhost:8080 (configured in Jetty)
- Frontend: http://localhost:4200 (Angular default)

## Database

### Location
Default: `todo.db` in project root

### Reset Database
```bash
# Stop backend
rm todo.db

# Restart backend - it will recreate and initialize database
mvn jetty:run
```

### Sample Data
On first run, database includes sample todos created by AppInitializationListener.

## Production Build

### Frontend
```bash
cd todo-angular
npm run build
# Output in dist/todo-angular/
```

### Backend
```bash
cd todo-server
mvn clean package -DskipTests
# Output in target/todo-server-0.0.1-SNAPSHOT.war
```

## Next Steps

1. ✅ Spring REST API created
2. ✅ Angular frontend created
3. ✅ Communication configured
4. **Now**: Run both servers and test
5. *Future*: Deploy to production environment

## Support

- See `MIGRATION_GUIDE.md` for migration details
- See `todo-angular/README_ANGULAR.md` for frontend docs
- Check backend logs for errors: `mvn jetty:run` console output
- Check frontend logs: Browser DevTools → Console tab

---

**Current Status**: ✅ Complete - Ready to run!

**Frontend**: Angular 16+ (TypeScript, RxJS)
**Backend**: Spring Web MVC 5.3 (Java 17)
**Database**: SQLite 3.45
**API**: REST JSON
**Architecture**: 2-panel layout (left 220px fixed, right flexible)
