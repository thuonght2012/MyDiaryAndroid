package com.example.mydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private RecyclerView rvTask;
    private TaskAdapter adapter;
    private TaskDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").build();
        btnAdd = findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddScreen();
            }
        });
        rvTask = findViewById(R.id.recycler_task);
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        rvTask.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new TaskAdapter();
        adapter.listener = new TaskAdapter.OnItemListener() {
            @Override
            public void onDeleteClicked(int position) {
                deleteItem(position);
            }

            @Override
            public void onClickItemUpdate(int position) {
                openUpdateDiaryScreen(adapter.taskList.get(position));
            }
        };
        rvTask.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAndDisplayAllTaskFromDatabase();
    }

    private void getAndDisplayAllTaskFromDatabase() {
        new AsyncTask<Void, Void, List<Task>>() {

            @Override
            protected List<Task> doInBackground(Void... voids) {

//                Task task1 = new Task("tap the duc", "20-12-2019");
//                db.taskDao().insert(task1);
//                Task task2 = new Task("Nau an", "30-12-2019");
//                db.taskDao().insert(task2);

                List<Task> tasks = db.taskDao().getAll();
                return tasks;
            }
            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                adapter.taskList = tasks;
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }


    private void deleteItem(final int deletePosition){
        final Task deleteTask = adapter.taskList.get(deletePosition);
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                db.taskDao().delete(deleteTask);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.taskList.remove(deletePosition);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
    private void openUpdateDiaryScreen(Task task) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("content", task.getDate());
        startActivity(intent);
    }






    private void openAddScreen(){
        Intent intent = new Intent( MainActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }
    }

