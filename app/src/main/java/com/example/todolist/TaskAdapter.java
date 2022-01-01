package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private LayoutInflater inflater;
    private int layout;
    private List<Task> tasks;
    private WorkDataBase db;

    public TaskAdapter(Context context, int resource, List<Task> tasks, WorkDataBase db) {
        super(context, resource, tasks);
        this.db = db;
        this.tasks = tasks;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        CheckBox checkBox = view.findViewById(R.id.checkbox_task);
        TextView nameTask = view.findViewById(R.id.task_name);
        TextView date = view.findViewById(R.id.date_task);

        Task task = tasks.get(position);

        checkBox.setChecked(task.getDone());
        nameTask.setText(task.getTaskName());
        date.setText(task.getDate());

        return view;
    }
}
