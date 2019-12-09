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
import android.widget.TextView;

public class UpdateActivity extends AppCompatActivity {

    TaskDatabase db;
    EditText edtTitle;
    EditText edtDate;
    Button btnUpdate;
    int id_item;
    Button btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").build();

        edtTitle = findViewById(R.id.taskUpdate);
        edtDate = findViewById(R.id.dateUpdate);
        btnUpdate = findViewById(R.id.btn_Update);
        btncancel = findViewById(R.id.btnBack);

        int id = getIntent().getIntExtra("id", 0);
        id_item = id;
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        edtTitle.setText(title);
        edtDate.setText(content);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodoToDatabase();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void updateTodoToDatabase() {
        final String title = edtTitle.getText().toString();
        final String content = edtDate.getText().toString();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Task diaryUpdated = new Task( title, content);
                diaryUpdated.setId(id_item);
                db.taskDao().updateOne(diaryUpdated);
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
                .setMessage("Successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}