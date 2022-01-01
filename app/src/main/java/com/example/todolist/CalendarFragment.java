package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
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

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {
    private ArrayList<Task> tasks = new ArrayList<Task>();
    ListView tasksList;
    private final String[] namesMonths = {"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ",
            "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    private final Date curDate = new Date();
    private WorkDataBase db;
    TaskAdapter taskAdapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = WorkDataBase.getInstance(getActivity().getBaseContext());

        tasksList = view.findViewById(R.id.tasks_list);

        showTasksForDay(curDate.getDate() + "-" + (curDate.getMonth() + 1) + "-" +
                (curDate.getYear() + 1900));

        tasksList.setOnItemClickListener((parent, v, position, id) -> {
            // получаем нажатый элемент
            Task task = taskAdapter.getItem(position);
            if(!tasksList.isItemChecked(position)) {
                task.setDone(!task.getDone());
                if (task.getDone()) {
                    db.changeDoneTask(task.getTaskName(), task.getDate(), 1);
                }
                else {
                    db.changeDoneTask(task.getTaskName(), task.getDate(), 0);
                }
                taskAdapter.notifyDataSetChanged();
            }
        });

        showMonth(view, curDate.getMonth(), curDate.getYear() + 1900, curDate.getDate());

        ImageView buttonRight = view.findViewById(R.id.arrow_right);
        buttonRight.setOnClickListener(v -> showNextMonth(view));

        ImageView buttonLeft = view.findViewById(R.id.arrow_left);
        buttonLeft.setOnClickListener(v -> showPreviousMonth(view));

        ImageButton buttonAddTask = view.findViewById(R.id.add_task_button);
        buttonAddTask.setOnClickListener(v -> onClickAddTask(view));
    }

    public void showTasksForDay(String date) {
        for(Task task: tasks) {
            taskAdapter.remove(task);
        }

        setInitData(date);

        taskAdapter = new TaskAdapter(getActivity(), R.layout.task_card, tasks, db);
        tasksList.setAdapter(taskAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNextMonth(View view) {
        TextView nameMonthView = view.findViewById(R.id.name_month);
        String textView = nameMonthView.getText().toString();
        String[] splitTextView = textView.split(" ");
        int ind = Arrays.asList(namesMonths).indexOf(splitTextView[0]);

        if (ind == 11) {
            showMonth(view, 0, Integer.parseInt(splitTextView[1]) + 1,
                    CellCalendar.activeCell.getDay());
        } else {
            showMonth(view, ind + 1, Integer.parseInt(splitTextView[1]),
                    CellCalendar.activeCell.getDay());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showPreviousMonth(View view) {
        TextView nameMonthView = view.findViewById(R.id.name_month);
        String textView = nameMonthView.getText().toString();
        String[] splitTextView = textView.split(" ");
        int ind = Arrays.asList(namesMonths).indexOf(splitTextView[0]);

        if (ind == 0) {
            showMonth(view, 11, Integer.parseInt(splitTextView[1]) - 1,
                    CellCalendar.activeCell.getDay());
        } else {
            showMonth(view, ind - 1, Integer.parseInt(splitTextView[1]),
                    CellCalendar.activeCell.getDay());
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showMonth(View view, int month, int year, Integer day) {
        Resources resources = getResources();

        TextView nameMonth = view.findViewById(R.id.name_month);
        nameMonth.setText(namesMonths[month] + " " + year);

        LinearLayout gridCalendar = view.findViewById(R.id.gridCalendar);
        gridCalendar.removeAllViews();

        Date curDateMonth = new GregorianCalendar(year, month, 1).getTime();

        int countDays = Month.of(month + 1).maxLength();
        int countDaysPrev;
        if (month == 0)
            countDaysPrev = Month.of(12).maxLength();
        else countDaysPrev = Month.of(month).maxLength();

        String[] daysOfWeek = {"пн", "вт", "ср", "чт", "пт", "сб", "вс"};

        LinearLayout line = new LinearLayout(getActivity());
        line.setOrientation(LinearLayout.HORIZONTAL);

        for(String dayOfWeek: daysOfWeek) {
            CellCalendar cell = new CellCalendar(getActivity(),this, dayOfWeek, 0,
                    15, resources.getColor(R.color.black, null), false, false);
            line.addView(cell);
        }

        gridCalendar.addView(line);

        int curDay = 1;
        int curDayNext = 1;

        for (int i = 0; i < 6; i++) {
            line = new LinearLayout(getActivity());
            line.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 7; j++) {
                CellCalendar cell;
                if (i == 0 && j < curDateMonth.getDay() - 1) {
                    cell = new CellCalendar(getActivity(), this,
                            Integer.toString(countDaysPrev - (curDateMonth.getDay() - 2) + j),
                            curDateMonth.getMonth(),
                            15, resources.getColor(R.color.gray, null),
                            true, false);
                } else if (curDay <= countDays) {
                    if (curDate.getMonth() == month && curDate.getDate() == curDay &&
                            day == curDay) {
                        cell = new CellCalendar(getActivity(), this, Integer.toString(curDay),
                                curDateMonth.getMonth() + 1, 15,
                                resources.getColor(R.color.cur_day, null),
                                true, true);
                    } else if (curDate.getMonth() == month && curDate.getDate() == curDay) {
                        cell = new CellCalendar(getActivity(), this, Integer.toString(curDay),
                                curDateMonth.getMonth() + 1, 15,
                                resources.getColor(R.color.cur_day, null),
                                true, false);
                    } else if (day != null && day == curDay) {
                        cell = new CellCalendar(getActivity(), this, Integer.toString(curDay),
                                curDateMonth.getMonth() + 1, 15,
                                resources.getColor(R.color.black, null),
                                true, true);
                    } else {
                        cell = new CellCalendar(getActivity(), this, Integer.toString(curDay),
                                curDateMonth.getMonth() + 1, 15,
                                resources.getColor(R.color.black, null),
                                true, false);
                    }
                    curDay++;
                } else {
                    cell = new CellCalendar(getActivity(), this, Integer.toString(curDayNext),
                            curDateMonth.getMonth() + 2,
                            15, resources.getColor(R.color.gray, null),
                            true, false);
                    curDayNext++;
                }
                line.addView(cell);
            }
            gridCalendar.addView(line);
        }
    }

    private void setInitData(String date) {
        ArrayList<ArrayList<String>> data = db.getTasksForDate(date);
        for (ArrayList<String> row: data) {
            Task task;
            if (row.get(2).equals("1"))
                task = new Task(row.get(1), true, row.get(3));
            else task = new Task(row.get(1), false, row.get(3));
            tasks.add(task);
        }
    }

    private void onClickAddTask(View view) {
        Intent intent = new Intent(view.getContext(), AddTaskActivity.class);

        TextView nameMonthView = view.findViewById(R.id.name_month);
        String[] splitTextView = nameMonthView.getText().toString().split(" ");
        int numMonth = Arrays.asList(namesMonths).indexOf(splitTextView[0]) + 1;
        String date = CellCalendar.activeCell.getDay() + "-" + numMonth + "-" +
                (curDate.getYear() + 1900);

        intent.putExtra("date", date);
        startActivity(intent);
    }
}