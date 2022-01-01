package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {
    private String date;
    private TextView inputTask;
    private ImageButton buttonAdd;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Bundle arguments = getIntent().getExtras();
        date = arguments.get("date").toString();

        inputTask = findViewById(R.id.input_task);
        buttonAdd = findViewById(R.id.add_task_button);

        inputTask.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    buttonAdd.setBackgroundResource(R.drawable.send_background);
                } else {
                    buttonAdd.setBackgroundResource(R.drawable.send_active_background);
                }
            }
        });
    }

    public void onClickAddTask(View view) {
        WorkDataBase db = WorkDataBase.getInstance(this);
        db.addTask(inputTask.getText().toString(), date);
        inputTask.setText("");
    }
}