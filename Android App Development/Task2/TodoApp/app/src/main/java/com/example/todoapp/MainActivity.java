package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.Listener {

    private DatabaseHelper db;
    private SessionManager session;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            goToLogin();
            return;
        }
        userId = session.getUserId();

        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Hi, " + session.getUserName());

        tvEmpty = findViewById(R.id.tvEmpty);
        recyclerView = findViewById(R.id.recyclerTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btnAddTask);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnAdd.setOnClickListener(v -> showAddTaskDialog());
        btnLogout.setOnClickListener(v -> logout());

        refreshTasks();
    }

    private void refreshTasks() {
        List<Task> tasks = db.getTasks(userId);
        adapter.setTasks(tasks);
        boolean empty = tasks.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void showAddTaskDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        EditText etTitle = view.findViewById(R.id.etTaskTitle);
        EditText etNotes = view.findViewById(R.id.etTaskNotes);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(view)
                .setPositiveButton("Add", null) // overridden below so the dialog stays open on error
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String title = etTitle.getText().toString().trim();
                    String notes = etNotes.getText().toString().trim();
                    if (title.isEmpty()) {
                        etTitle.setError("Task name is required");
                        return;
                    }
                    db.addTask(userId, title, notes);
                    refreshTasks();
                    dialog.dismiss();
                }));
        dialog.show();
    }

    @Override
    public void onToggle(Task task, boolean completed) {
        db.setCompleted(userId, task.id, completed);
        // Post so the list isn't rebuilt in the middle of the click callback
        recyclerView.post(this::refreshTasks);
    }

    @Override
    public void onDelete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete task?")
                .setMessage("\"" + task.title + "\" will be deleted permanently.")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteTask(userId, task.id);
                    refreshTasks();
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logout() {
        session.logout();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
