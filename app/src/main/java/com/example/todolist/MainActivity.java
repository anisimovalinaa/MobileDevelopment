package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridCalendar = findViewById(R.id.gridCalendar);

//        Date curDate = new Date();
        Date curDate = new GregorianCalendar(2021, 9, 1).getTime();
        Date beginDate = new GregorianCalendar(curDate.getYear() + 1900,
                curDate.getMonth(), 1).getTime();

        int countDays = Month.of(curDate.getMonth() + 1).maxLength();
        int countDaysLast = Month.of(curDate.getMonth()).maxLength();

        int widthCell = getWindowManager().getDefaultDisplay().getWidth()/7;

        String[] daysOfWeek = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};

        for(String day: daysOfWeek) {
            CellCalendar cell = new CellCalendar(this, day, 20, widthCell, R.color.black);
            gridCalendar.addView(cell);
        }

        int curDay = 1;
        int curDayNext = 1;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                CellCalendar cell;
                if (i == 0) {
                    if (j < beginDate.getDay() - 1) {
                        cell = new CellCalendar(this,
                                Integer.toString(countDaysLast - (beginDate.getDay() - 2) + j),
                                20, widthCell, R.color.gray);
                    } else {
                        cell = new CellCalendar(this, Integer.toString(curDay), 20,
                                widthCell, R.color.black);
                        curDay++;
                    }
                } else {
                    if (curDay <= countDays) {
                        cell = new CellCalendar(this, Integer.toString(curDay), 20,
                                widthCell, R.color.black);
                        curDay++;
                    } else {
                        cell = new CellCalendar(this, Integer.toString(curDayNext),
                                20, widthCell, R.color.gray);
                        curDayNext++;
                    }
                }
                gridCalendar.addView(cell);
            }
        }
    }
}