package com.example.mydiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {

    TaskDatabase db;
    EditText editTitle;
    EditText editDate;
    Button btnUpdate;
    int taskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").build();

        editTitle = findViewById(R.id.taskUpdate);
        editDate = findViewById(R.id.dateUpdate);

        @SuppressLint("WrongViewCast") Button btn = (Button) findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").build();

        btnUpdate = findViewById(R.id.btn_Update);

        int id = getIntent().getIntExtra("id", 0);
        taskId = id;
        String title = getIntent().getStringExtra("task");
        String date = getIntent().getStringExtra("date");

        editTitle.setText(title);
        editDate.setText(date);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodoToDatabase();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void updateTodoToDatabase() {
        final String taskTitle = editTitle.getText().toString();
        final String taskDate = editDate.getText().toString();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Task newTask = new Task();
                newTask.setTitle(taskTitle);
                newTask.setDate(taskDate);
                newTask.setId(taskId);
                db.taskDao().updateOne(newTask);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showSuccessDialog();
            }
        }.execute();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage("Update Success")
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    private void showDatePicker(){
        DatePickerDialog date = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                @SuppressLint("WrongViewCast") EditText editDate = findViewById(R.id.dateUpdate);
                editDate.setText(i+" "+(i1+1)+" "+i2);
            }
        }, 2019, 01, 01);
        date.show();
    }
}