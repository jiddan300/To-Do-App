package com.dicoding.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()

    private lateinit var etTitle : TextView
    private lateinit var etDesc : TextView

    private lateinit var addTaskViewModel : AddTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)

        etTitle = findViewById(R.id.add_ed_title)
        etDesc = findViewById(R.id.add_ed_description)

        val factory = ViewModelFactory.getInstance(this)
        addTaskViewModel = ViewModelProvider(this, factory).get(AddTaskViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()

                if(title.isNotEmpty() && desc.isNotEmpty()){
                    val task = Task(
                        id = 0,
                        title = title,
                        description = desc,
                        dueDateMillis = dueDateMillis,
                        isCompleted = false
                    )

                    addTaskViewModel.addTask(task)
                    finish()
                }else{
                    Toast.makeText(this, "Title and Description should not be empty", Toast.LENGTH_LONG).show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}