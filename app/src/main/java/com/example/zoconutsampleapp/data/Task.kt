package com.example.zoconutsampleapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_details")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var status: Boolean,
    var task: String
)