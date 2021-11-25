package com.example.todolist;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CellCalendar extends androidx.appcompat.widget.AppCompatTextView implements View.OnClickListener {
    public CellCalendar(AppCompatActivity context, String text, int sizeText, int width, int color) {
        super(context);
        setText(text);
        setWidth(width);
        setGravity(Gravity.CENTER);
        setTextSize(sizeText);
        setTextColor(color);
        setClickable(true);
        setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        setBackgroundResource(R.drawable.cell_calendar_background);
    }
}
