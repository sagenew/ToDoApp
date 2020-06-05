package com.example.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoTaskDao {

    @Insert
    fun insert(task: ToDoTask)

    @Update
    fun update(task: ToDoTask)

    @Delete
    fun delete(task: ToDoTask)

    @Query("SELECT * from to_do_task_table WHERE taskId = :key")
    fun get(key: Long): ToDoTask?

    @Query("DELETE FROM to_do_task_table")
    fun clear()

    @Query("SELECT * FROM to_do_task_table ORDER BY taskId DESC")
    fun getAllTasks(): LiveData<List<ToDoTask>>

    @Query("SELECT * FROM to_do_task_table ORDER BY taskId DESC LIMIT 1")
    fun getLastTask(): ToDoTask?

    @Query("SELECT * from to_do_task_table WHERE taskId = :key")
    fun getTaskWithId(key: Long): LiveData<ToDoTask>

}