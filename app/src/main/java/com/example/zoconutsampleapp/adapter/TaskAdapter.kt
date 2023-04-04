package com.example.zoconutsampleapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.zoconutsampleapp.R
import com.example.zoconutsampleapp.data.Task

class TaskAdapter(private val context: Context, private val onClickItem: OnClickItem) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var arrTask = ArrayList<Task>()
    lateinit var edtAddTask: Button
    lateinit var edtTaskDes: EditText


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskStatus: CheckBox
        var taskDescription: TextView

        init {
            taskStatus = itemView.findViewById(R.id.task_status)
            taskDescription = itemView.findViewById(R.id.task_description)
        }

        fun bind(info: Task) {
            taskStatus.isChecked = info.status
            taskDescription.text = info.task

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_recyclerview_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = arrTask[position]
        holder.bind(pos)
        holder.taskStatus.setOnClickListener {
            arrTask.get(position).status = holder.taskStatus.isChecked
            onClickItem.updateStatus(arrTask.get(position),position)
        }
        holder.itemView.setOnClickListener {
            val popup = PopupMenu(it.context, holder.itemView)
            popup.menuInflater.inflate(R.menu.menu_recyclerview_item, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        val mDialogView = LayoutInflater.from(it.context).inflate(R.layout.edit_task, null)
                        val builder = AlertDialog.Builder(it.context)
                            .setView(mDialogView)
                        val mAlterDialog = builder.show()
                        edtTaskDes = mAlterDialog.findViewById(R.id.updateTaskName)
                        edtAddTask = mAlterDialog.findViewById(R.id.updateTask)
                        edtAddTask.setOnClickListener {
                            mAlterDialog.dismiss()
                            arrTask.get(position).task = edtTaskDes.text.toString()
                            onClickItem.updateTask(arrTask.get(position),position)
                        }
                    }
                    R.id.delete -> onClickItem.deleteRow(arrTask.get(position),position)
                }
                false
            }
            popup.show()
        }

    }

    override fun getItemCount(): Int {
        return arrTask.size
    }

    fun updateUserList(user: List<Task>) {
        arrTask.clear()
        arrTask.addAll(user)
        notifyDataSetChanged()
    }

    interface OnClickItem {
        fun deleteRow(task: Task, position: Int)
        fun updateStatus(task: Task, position: Int)
        fun updateTask(task: Task, position: Int)
    }

}