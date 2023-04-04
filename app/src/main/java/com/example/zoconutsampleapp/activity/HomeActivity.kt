package com.example.zoconutsampleapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.TaskApplication
import com.example.zoconutsampleapp.adapter.TaskAdapter
import com.example.zoconutsampleapp.data.Task
import com.example.zoconutsampleapp.databinding.ActivityHomeBinding
import com.example.zoconutsampleapp.model.TaskViewModel
import com.example.zoconutsampleapp.model.TaskViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeActivity : AppCompatActivity(), TaskAdapter.OnClickItem {
    lateinit var binding: ActivityHomeBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var taskViewModel: TaskViewModel
    lateinit var adapter: TaskAdapter
    private lateinit var taskDes: EditText
    private lateinit var addTask: Button
    private lateinit var dbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        firebaseAuth = FirebaseAuth.getInstance()
        setSupportActionBar(binding.toolbar)

        binding.shimmer.startShimmer()
        binding.homeRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.homeRecyclerview.setHasFixedSize(true)
        adapter = TaskAdapter(this, this)
        binding.homeRecyclerview.adapter = adapter


        val repository = (application as TaskApplication).taskRepository
        taskViewModel =
            ViewModelProvider(this, TaskViewModelFactory(repository)).get(TaskViewModel::class.java)

        taskViewModel.getTask().observe(this, Observer {
            Handler().postDelayed({
                binding.shimmer.stopShimmer()
                binding.shimmer.visibility = View.GONE
                binding.homeRecyclerview.visibility = View.VISIBLE

            }, 1000)

            adapter.updateUserList(it)

        })

        binding.add.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_new_task, null)
            val builder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlterDialog = builder.show()
            taskDes = mAlterDialog.findViewById(R.id.taskName)
            addTask = mAlterDialog.findViewById(R.id.addTask)
            addTask.setOnClickListener {
                mAlterDialog.dismiss()
                val taskName = taskDes.text.toString()
                taskViewModel.insertTask(Task(null, false, taskName))

            }
        }

        getFirebaseData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.logout) {
            firebaseAuth.signOut()
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putBoolean("flag", false)
            editor?.apply()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun deleteRow(task: Task, position: Int) {
        taskViewModel.deleteTask(task)
        adapter.notifyItemChanged(position)
    }

    override fun updateStatus(task: Task, position: Int) {
        taskViewModel.updateTaskStatus(task)
        adapter.notifyItemChanged(position)
    }

    override fun updateTask(task: Task, position: Int) {
        taskViewModel.updateTask(task)
        adapter.notifyItemChanged(position)
    }

    private fun getFirebaseData() {
        dbref = FirebaseDatabase.getInstance().getReference("tasks")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        val task = userSnapshot.getValue(Task::class.java)
                        taskViewModel.insertTask(task!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}