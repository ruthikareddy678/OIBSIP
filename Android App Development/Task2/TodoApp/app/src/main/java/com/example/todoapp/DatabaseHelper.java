package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password_hash TEXT NOT NULL)");

        db.execSQL("CREATE TABLE tasks ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "title TEXT NOT NULL, "
                + "notes TEXT, "
                + "completed INTEGER NOT NULL DEFAULT 0, "
                + "created_at INTEGER NOT NULL, "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ---------------------------------------------------------------- users

    public boolean emailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("users", new String[]{"id"}, "email = ?",
                new String[]{email}, null, null, null)) {
            return c.moveToFirst();
        }
    }

    /** Returns true if the account was created, false if the email is already registered. */
    public boolean registerUser(String name, String email, String password) {
        if (emailExists(email)) return false;
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password_hash", PasswordUtil.hash(password));
        return getWritableDatabase().insert("users", null, values) != -1;
    }

    /** Returns {id, name} of the user if the credentials are valid, otherwise null. */
    public Object[] authenticate(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("users", new String[]{"id", "name", "password_hash"},
                "email = ?", new String[]{email}, null, null, null)) {
            if (c.moveToFirst() && PasswordUtil.verify(password, c.getString(2))) {
                return new Object[]{c.getLong(0), c.getString(1)};
            }
            return null;
        }
    }

    // ---------------------------------------------------------------- tasks

    public long addTask(long userId, String title, String notes) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("title", title);
        values.put("notes", notes);
        values.put("completed", 0);
        values.put("created_at", System.currentTimeMillis());
        return getWritableDatabase().insert("tasks", null, values);
    }

    /** All tasks of one user: pending first (newest first), completed at the bottom. */
    public List<Task> getTasks(long userId) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("tasks", new String[]{"id", "title", "notes", "completed"},
                "user_id = ?", new String[]{String.valueOf(userId)},
                null, null, "completed ASC, created_at DESC")) {
            while (c.moveToNext()) {
                list.add(new Task(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3) == 1));
            }
        }
        return list;
    }

    public void setCompleted(long userId, long taskId, boolean completed) {
        ContentValues values = new ContentValues();
        values.put("completed", completed ? 1 : 0);
        getWritableDatabase().update("tasks", values, "id = ? AND user_id = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }

    public void deleteTask(long userId, long taskId) {
        getWritableDatabase().delete("tasks", "id = ? AND user_id = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }
}
