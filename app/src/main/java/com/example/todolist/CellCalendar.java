package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@SuppressLint("ViewConstructor")
public class CellCalendar extends androidx.appcompat.widget.AppCompatTextView implements View.OnClickListener {
    static CellCalendar activeCell;
    private final int colorCell;
    private String day;
    private int month;
    private boolean clickable;
    private CalendarFragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public CellCalendar(FragmentActivity context, CalendarFragment fragment, String day, int month,
                        int sizeText, int color,
                        boolean clickable, boolean active) {
        super(context);

        colorCell = color;
        this.day = day;
        this.month = month;
        this.clickable = clickable;
        this.fragment = fragment;

        setText(day);
        setGravity(Gravity.CENTER);
        setTextSize(sizeText);
        setTextColor(color);
        setClickable(clickable);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        layoutParams.setMargins(7, 3, 7, 3);
        setLayoutParams(layoutParams);

        if (active) {
            makeActive();
        }

        setPadding(2, 7, 2, 7);
        setOnClickListener(this);
    }

    public int getDay() {
        return Integer.parseInt(day);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void makeNotActive() {
        Resources resources = getResources();
        setTextColor(colorCell);
        setBackgroundColor(resources.getColor(R.color.back_calendar, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void makeActive() {
        CellCalendar.activeCell = this;
        setBackgroundResource(R.drawable.cell_calendar_background);
        setTextColor(Color.parseColor("#FFFFFFFF"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (clickable) {
            if (CellCalendar.activeCell != null) {
                CellCalendar.activeCell.makeNotActive();
            }
            makeActive();

            Date curDate = new Date();
            String date = day + "-" + month + "-" + (curDate.getYear() + 1900);

            fragment.showTasksForDay(date);
        }
    }
}
