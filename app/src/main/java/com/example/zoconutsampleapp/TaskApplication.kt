package com.example.zoconutsampleapp

import android.app.Application
import com.example.zoconutsampleapp.data.TaskDatabase
import com.example.zoconutsampleapp.repository.TaskRepository

class TaskApplication : Application() {
    lateinit var taskRepository: TaskRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val database = TaskDatabase.getDatabase(applicationContext)
        taskRepository = TaskRepository( database, applicationContext)
    }
}