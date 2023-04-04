package com.example.zoconutsampleapp.repository

import androidx.lifecycle.LiveData
import com.example.zoconutsampleapp.data.Task
import com.example.zoconutsampleapp.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {


    fun getTask(): LiveData<List<Task>> {
        return taskDao.getTask()
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun updateTaskStatus(task: Task) {
        taskDao.updateTaskStatus(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}