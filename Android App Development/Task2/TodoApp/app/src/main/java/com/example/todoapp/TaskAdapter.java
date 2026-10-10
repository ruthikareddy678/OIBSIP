package com.example.todoapp;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {

    public interface Listener {
        void onToggle(Task task, boolean completed);
        void onDelete(Task task);
    }

    private final List<Task> tasks = new ArrayList<>();
    private final Listener listener;

    public TaskAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Task task = tasks.get(position);

        h.title.setText(task.title);
        if (task.notes == null || task.notes.isEmpty()) {
            h.notes.setVisibility(View.GONE);
        } else {
            h.notes.setVisibility(View.VISIBLE);
            h.notes.setText(task.notes);
        }

        // Avoid triggering the old listener while recycling views
        h.check.setOnCheckedChangeListener(null);
        h.check.setChecked(task.completed);
        applyStrike(h.title, task.completed);
        applyStrike(h.notes, task.completed);
        h.itemView.setAlpha(task.completed ? 0.55f : 1f);

        h.check.setOnCheckedChangeListener((button, isChecked) -> listener.onToggle(task, isChecked));
        h.delete.setOnClickListener(v -> listener.onDelete(task));
    }

    private void applyStrike(TextView tv, boolean done) {
        if (done) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final CheckBox check;
        final TextView title, notes;
        final Button delete;

        VH(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.cbDone);
            title = itemView.findViewById(R.id.tvTaskTitle);
            notes = itemView.findViewById(R.id.tvTaskNotes);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
