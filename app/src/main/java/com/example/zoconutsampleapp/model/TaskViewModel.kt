package com.example.zoconutsampleapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoconutsampleapp.data.Task
import com.example.zoconutsampleapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel(){

    fun getTask(): LiveData<List<Task>> {
        return taskRepository.getTask()
    }

    fun insertTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insertTask(task)
        }
    }
    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }
    fun updateTaskStatus(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTaskStatus(task)
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(task)
        }
    }
}