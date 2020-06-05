package com.example.todoapp.todolist

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.todoapp.R
import com.example.todoapp.database.ToDoTaskDatabase
import com.example.todoapp.databinding.FragmentToDoListBinding

class ToDoListFragment : Fragment() {

    lateinit var toDoListViewModel : ToDoListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentToDoListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_to_do_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ToDoTaskDatabase.getInstance(application).toDoTaskDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        toDoListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ToDoListViewModel::class.java)

        binding.toDoListViewModel = toDoListViewModel

        binding.lifecycleOwner = this

        val adapter = ToDoListAdapter(toDoListViewModel)
        binding.taskList.adapter = adapter

        toDoListViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.tasks = it
            }
        })

        binding.fab.setOnClickListener {
            onClickAdd(it)
        }

        toDoListViewModel.navigateToEditTask.observe(this, Observer { taskId ->
            taskId?.let {
                toDoListViewModel.doneEditTaskNavigating()
                onClickUpdate(taskId)
            }
        })

        toDoListViewModel.navigateToDeleteTask.observe(this, Observer { taskId ->
            taskId?.let {
                toDoListViewModel.doneDeleteTaskNavigating()
                onClickDelete(taskId)
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
            inflater!!.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.action_clear) {
            toDoListViewModel.onClearToDoTasks()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickAdd(it : View) {
        val dialog = AlertDialog.Builder(it.context)
        val view = layoutInflater.inflate(R.layout.dialog_add_to_do_task, null)
        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        val taskName = view.findViewById<EditText>(R.id.task_name)
        val taskDescription = view.findViewById<EditText>(R.id.task_description)
        val addTitle = "Add New Task"
        dialogTitle.text = addTitle
        dialog.setView(view)
        dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
            if (taskName.text.isNotEmpty()) {
                toDoListViewModel.onAddToDoTask(taskName.text.toString(),taskDescription.text.toString());
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

        }
        dialog.show()
    }


    private fun onClickUpdate(taskId : Long) {
        val dialog = AlertDialog.Builder(view!!.context)
        val view = layoutInflater.inflate(R.layout.dialog_add_to_do_task, null)
        val task = toDoListViewModel.getTaskById(taskId);
        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        val taskName = view.findViewById<EditText>(R.id.task_name)
        val taskDescription = view.findViewById<EditText>(R.id.task_description)
        if (task != null) {
            val updateTitle = "Update To-Do Task"
            dialogTitle.text = updateTitle
            taskName.setText(task.name)
            taskDescription.setText(task.description)
            dialog.setView(view)
            dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
                if (taskName.text.isNotEmpty()) {
                    task.name = taskName.text.toString()
                    task.description = taskDescription.text.toString()
                    toDoListViewModel.onUpdateToDoTask(task)
                }
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()
    }

    private fun onClickDelete(taskId : Long) {
        val dialog = AlertDialog.Builder(view!!.context)
        dialog.setTitle("Are you sure")
        dialog.setMessage("Do you want to delete this item ?")
        dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
            toDoListViewModel.onDeleteTaskById(taskId)
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()

    }
}