package com.example.mydiary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * from task")
    List<Task> getAll();

    @Insert
    void insert(Task task);

    @Delete
    Void delete(Task task);
    @Update
    void updateOne(Task task);
}