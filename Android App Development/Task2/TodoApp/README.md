# To-Do App with Login (Android)

A personal task manager. Users register and log in before they can see their own to-do list. All data is stored locally in SQLite. Built with Java, XML and SQLite in Android Studio.

## Features

- Sign-up screen: name, email, password, Register button (email format and minimum password length validated)
- Login screen: email and password fields, Login button
- Passwords are never stored in plain text: SHA-256 with a random per-user salt, stored as `salt:hash`
- Session kept in SharedPreferences; the app opens straight to the task list if you are still logged in
- Logout button clears the session and returns to the login screen
- Task list shows only the logged-in user's tasks
- "Add Task" opens a dialog with a task name (required) and optional notes
- Tick a task to mark it complete (strikethrough, dimmed, moved to the bottom)
- Delete a task permanently (with a confirmation dialog)
- Friendly empty state when there are no tasks

## Database Schema

```
users(id PK, name, email UNIQUE, password_hash)
tasks(id PK, user_id FK -> users.id ON DELETE CASCADE, title, notes, completed, created_at)
```

Every task query filters by `user_id`, so one user can never see or change another user's tasks.

## Project Structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/todoapp/
│   ├── LoginActivity.java
│   ├── SignupActivity.java
│   ├── MainActivity.java        (task list)
│   ├── TaskAdapter.java
│   ├── Task.java
│   ├── DatabaseHelper.java      (SQLite)
│   ├── PasswordUtil.java        (salted SHA-256)
│   └── SessionManager.java      (login session)
└── res/layout/
    ├── activity_login.xml
    ├── activity_signup.xml
    ├── activity_main.xml
    ├── item_task.xml
    └── dialog_add_task.xml
```

## Setup

1. In Android Studio: New Project > Empty Views Activity, Language **Java**, Name `TodoApp`, Package `com.example.todoapp`.
2. Copy the files above into the matching folders (overwrite the generated `MainActivity.java`, `activity_main.xml` and `AndroidManifest.xml`).
3. Sync Gradle and run on an emulator or a phone.

## Tech Stack

- Java, XML layouts
- SQLite (`SQLiteOpenHelper`)
- RecyclerView for the task list

## Screenshots

![alt text](<Screenshot 2026-10-10 150030.png>)
![alt text](<Screenshot 2026-10-10 150054.png>)
![alt text](<Screenshot 2026-10-10 150818.png>)

## Author

Kakulavarm Ruthika
