package com.example.mydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {
    private EditText edtTitle;
    private EditText edtDate;
    private Button btnAdd;
    private TaskDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").build();
        btnAdd = findViewById(R.id.button_add);
        edtTitle = findViewById(R.id.editTask);
        edtDate = findViewById(R.id.editDate);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskToDatabase();
            }
        });

        edtDate.setFocusable(false);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
    }

    private void pickDate() {
        new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = "" + year + "/" + (month + 1) + "/" + day;
                edtDate.setText(date);

            }
        }, 2019, 11, 1).show();
    }

    private void addTaskToDatabase() {
        final Task task = new Task();
        task.setTitle(edtTitle.getText().toString());
        task.setDate(edtDate.getText().toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.taskDao().insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddTaskActivity.this, "Add task successful", Toast.LENGTH_SHORT).show();
                closeScreen();
            }
        }.execute();
    }
    private void closeScreen() {
        finish();
    }

}
