package com.example.todoapp.todolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.ToDoTask


class ToDoListAdapter(private val toDoListViewModel: ToDoListViewModel) :
    RecyclerView.Adapter<ToDoListAdapter.ToDoTaskViewHolder>() {
    var tasks = listOf<ToDoTask>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ToDoTaskViewHolder, position: Int) {
        val item = tasks[position]
        holder.toDoName.text = item.name
        holder.toDoDescription.text = item.description
        holder.toDoStatus.isChecked = item.status
        holder.toDoStatus.setOnClickListener {
            tasks[position].status = !tasks[position].status
            if(tasks[position].status) holder.toDoName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            else holder.toDoName.paintFlags =
                holder.toDoName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            toDoListViewModel.onUpdateToDoTask(tasks[position])
        }

        holder.toDoAttributes.setOnClickListener {
            toDoListViewModel.onEditTaskClicked(item.taskId)
        }
        holder.toDoDeleteTask.setOnClickListener {
            toDoListViewModel.onDeleteTaskClicked(item.taskId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_to_do_task, parent, false)
        return ToDoTaskViewHolder(view)
    }

    class ToDoTaskViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val toDoStatus: CheckBox = v.findViewById(R.id.status_checkbox)
        val toDoName: TextView = v.findViewById(R.id.task_name)
        val toDoDescription: TextView = v.findViewById(R.id.task_description)
        val toDoAttributes: LinearLayout = v.findViewById(R.id.task_attributes)
        val toDoDeleteTask: ImageView = v.findViewById(R.id.task_delete)
    }
}