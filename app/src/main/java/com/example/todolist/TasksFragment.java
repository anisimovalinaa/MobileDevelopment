package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class TasksFragment extends Fragment {
    ArrayList<Task> tasksToday = new ArrayList<Task>();
    ArrayList<Task> tasksNotCompleted = new ArrayList<Task>();
    ArrayList<Task> tasksCompleted = new ArrayList<Task>();
    ListView tasksTodayList, tasksNotCompletedList, tasksCompletedList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitDataToday();
        setInitDataCompleted();
        setInitDataNotCompleted();

        onClickTasks(view, view.findViewById(R.id.tasks_today));
        onClickTasks(view, view.findViewById(R.id.tasks_not_completed));
        onClickTasks(view, view.findViewById(R.id.tasks_completed));

        tasksTodayList = view.findViewById(R.id.magic_tasks_today);
        tasksNotCompletedList = view.findViewById(R.id.magic_tasks_not_completed);
        tasksCompletedList = view.findViewById(R.id.magic_tasks_completed);

        WorkDataBase db = WorkDataBase.getInstance(getActivity().getBaseContext());

        TaskAdapter taskAdapterToday = new TaskAdapter(getActivity(), R.layout.task_card, tasksToday, db);
        tasksTodayList.setAdapter(taskAdapterToday);

        TaskAdapter taskAdapterNotCompleted = new TaskAdapter(getActivity(), R.layout.task_card, tasksNotCompleted, db);
        tasksNotCompletedList.setAdapter(taskAdapterNotCompleted);

        TaskAdapter taskAdapterCompleted = new TaskAdapter(getActivity(), R.layout.task_card, tasksCompleted, db);
        tasksCompletedList.setAdapter(taskAdapterCompleted);

        tasksCompletedList.setOnItemClickListener((parent, v, position, id) -> {
            // получаем нажатый элемент
            Task task = taskAdapterCompleted.getItem(position);
            System.out.println(task.getDone());
            if(!tasksCompletedList.isItemChecked(position)) {
                db.changeDoneTask(task.getTaskName(), task.getDate(), 0);
                taskAdapterCompleted.remove(task);
                taskAdapterNotCompleted.add(task);
            }
        });

        tasksNotCompletedList.setOnItemClickListener((parent, v, position, id) -> {
            // получаем нажатый элемент
            Task task = taskAdapterNotCompleted.getItem(position);
            if(!tasksNotCompletedList.isItemChecked(position)) {
                task.setDone(true);
                db.changeDoneTask(task.getTaskName(), task.getDate(), 1);
                taskAdapterNotCompleted.remove(task);
                taskAdapterCompleted.add(task);
            }
        });

        ImageButton buttonAddTask = view.findViewById(R.id.add_task_button);
        buttonAddTask.setOnClickListener(v -> onClickAddTask(view));
    }

    private void setInitDataToday() {
        WorkDataBase db = WorkDataBase.getInstance(getActivity().getBaseContext());
        ArrayList<ArrayList<String>> data = db.getTasksForToday();
        for (ArrayList<String> row: data) {
            Task task;
            if (row.get(2).equals("1"))
                task = new Task(row.get(1), true, row.get(3));
            else task = new Task(row.get(1), false, row.get(3));
            tasksToday.add(task);
        }
    }

    private void setInitDataNotCompleted() {
        WorkDataBase db = WorkDataBase.getInstance(getActivity().getBaseContext());
        ArrayList<ArrayList<String>> data = db.getTasksNotCompleted();
        for (ArrayList<String> row: data) {
            Task task;
            if (row.get(2).equals("1"))
                task = new Task(row.get(1), true, row.get(3));
            else task = new Task(row.get(1), false, row.get(3));
            tasksNotCompleted.add(task);
        }
    }

    private void setInitDataCompleted() {
        WorkDataBase db = WorkDataBase.getInstance(getActivity().getBaseContext());
        ArrayList<ArrayList<String>> data = db.getTasksCompleted();
        for (ArrayList<String> row: data) {
            Task task;
            if (row.get(2).equals("1"))
                task = new Task(row.get(1), true, row.get(3));
            else task = new Task(row.get(1), false, row.get(3));
            tasksCompleted.add(task);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void onClickTasks(View view, LinearLayout tasks) {
        ListView magic_layout = null;
        ImageView arrow = null;

        switch (tasks.getId()) {
            case R.id.tasks_today: {
                magic_layout = view.findViewById(R.id.magic_tasks_today);
                arrow = view.findViewById(R.id.arrow_tasks_today);
            } break;
            case R.id.tasks_not_completed: {
                magic_layout = view.findViewById(R.id.magic_tasks_not_completed);
                arrow = view.findViewById(R.id.arrow_tasks_not_completed);
            } break;
            case R.id.tasks_completed: {
                magic_layout = view.findViewById(R.id.magic_tasks_completed);
                arrow = view.findViewById(R.id.arrow_tasks_completed);
            } break;
        }

        ListView finalMagic_layout = magic_layout;
        ImageView finalArrow = arrow;
        tasks.setOnClickListener(v -> {
            if (finalMagic_layout != null && finalMagic_layout.getVisibility() == View.VISIBLE) {
                finalMagic_layout.setVisibility(View.GONE);
                finalArrow.setImageResource(R.drawable.arrow_down);
            } else {
                assert finalMagic_layout != null;
                finalMagic_layout.setVisibility(View.VISIBLE);
                finalArrow.setImageResource(R.drawable.arrow_up);
            }
        });
    }

    private void onClickAddTask(View view) {
        Intent intent = new Intent(view.getContext(), AddTaskActivity.class);

        Date curDate = new Date();
        String date = curDate.getDate() + "-" + (curDate.getMonth() + 1) + "-" +
                (curDate.getYear() + 1900);

        intent.putExtra("date", date);
        startActivity(intent);
    }
}