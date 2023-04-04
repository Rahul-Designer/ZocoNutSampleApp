package com.example.zoconutsampleapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.zoconutsampleapp.data.Task
import com.example.zoconutsampleapp.data.TaskDao
import com.example.zoconutsampleapp.data.TaskDatabase

class TaskRepository(
    private val taskDatabase: TaskDatabase,
    private val applicationContext: Context
) {

    fun getTask(): LiveData<List<Task>> {
        return taskDatabase.taskDao().getTask()
    }

    suspend fun insertTask(task: Task) {
        taskDatabase.taskDao().insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDatabase.taskDao().updateTask(task)
    }

    suspend fun updateTaskStatus(task: Task) {
        taskDatabase.taskDao().updateTaskStatus(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDatabase.taskDao().deleteTask(task)
    }
}