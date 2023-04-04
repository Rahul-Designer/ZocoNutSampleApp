package com.example.zoconutsampleapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_details ORDER BY ID ASC")
    fun getTask() : LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Update
    fun updateTaskStatus(task: Task)
}