package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"NonConstantResourceId", "ResourceAsColor", "ResourceType"})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_tasks: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, TasksFragment.class, null)
                        .commit();

                makeActiveButtonMenu("tasks");
            } break;
            case R.id.button_calendar: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, CalendarFragment.class, null)
                        .commit();

                makeActiveButtonMenu("calendar");
            } break;
        }
    }

    private void makeActiveButtonMenu(String button) {
        switch (button) {
            case "tasks": {
                ImageView imageNonActive = findViewById(R.id.image_calendar_button);
                imageNonActive.setColorFilter(getResources().getColor(R.color.menu_nonactive));
                TextView textNonActive = findViewById(R.id.text_calendar_button);
                textNonActive.setTextColor(getResources().getColor(R.color.menu_nonactive));

                ImageView image = findViewById(R.id.image_tasks_button);
                image.setColorFilter(getResources().getColor(R.color.blue));
                TextView text = findViewById(R.id.text_tasks_button);
                text.setTextColor(getResources().getColor(R.color.blue));
            } break;
            case "calendar": {
                ImageView imageNonActive = findViewById(R.id.image_tasks_button);
                imageNonActive.setColorFilter(getResources().getColor(R.color.menu_nonactive));
                TextView textNonActive = findViewById(R.id.text_tasks_button);
                textNonActive.setTextColor(getResources().getColor(R.color.menu_nonactive));

                ImageView image = findViewById(R.id.image_calendar_button);
                image.setColorFilter(getResources().getColor(R.color.blue));
                TextView text = findViewById(R.id.text_calendar_button);
                text.setTextColor(getResources().getColor(R.color.blue));
            }
        }
    }
}