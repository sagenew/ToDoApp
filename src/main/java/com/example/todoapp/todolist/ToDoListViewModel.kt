package com.example.todoapp.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.database.ToDoTask
import com.example.todoapp.database.ToDoTaskDao

class ToDoListViewModel (
        val database: ToDoTaskDao,
        application: Application) : AndroidViewModel(application) {

    val tasks = database.getAllTasks()

    val navigateToEditTask: LiveData<Long>
        get() = _navigateToEditTask

    val navigateToDeleteTask: LiveData<Long>
        get() = _navigateToDeleteTask

    private val _navigateToEditTask = MutableLiveData<Long>()
    private val _navigateToDeleteTask = MutableLiveData<Long>()

    fun onAddToDoTask(name : String, description : String) {
        val toDoTask = ToDoTask()
        toDoTask.name = name;
        toDoTask.description = description;
        database.insert(toDoTask);
    }

    fun onUpdateToDoTask(task: ToDoTask) {
        database.update(task);
    }

    fun getTaskById(id: Long) : ToDoTask? {
        return database.get(id)
    }

    fun onClearToDoTasks() {
        database.clear()
    }

    fun onDeleteTaskById(id: Long) {
        database.get(id)?.let { database.delete(it) }
    }

    fun onEditTaskClicked(id: Long) {
        _navigateToEditTask.value = id
    }

    fun doneEditTaskNavigating() {
        _navigateToEditTask.value = null
    }

    fun onDeleteTaskClicked(id: Long) {
        _navigateToDeleteTask.value = id
    }

    fun doneDeleteTaskNavigating() {
        _navigateToDeleteTask.value = null
    }




}