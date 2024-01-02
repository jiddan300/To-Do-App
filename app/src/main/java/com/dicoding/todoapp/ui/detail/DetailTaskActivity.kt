package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var title : TextInputEditText
    private lateinit var desc : TextInputEditText
    private lateinit var dueDate : TextInputEditText
    private lateinit var btnDelete : Button

    private lateinit var detailtaskViewModel : DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        title= findViewById(R.id.detail_ed_title)
        desc = findViewById(R.id.detail_ed_description)
        dueDate = findViewById(R.id.detail_ed_due_date)
        btnDelete = findViewById(R.id.btn_delete_task)

        val factory = ViewModelFactory.getInstance(this)
        detailtaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        detailtaskViewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))

        detailtaskViewModel.task.observe(this){
            if (it != null){
                title.setText(it.title)
                desc.setText(it.description)
                dueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
            }
        }

        btnDelete.setOnClickListener {
            detailtaskViewModel.deleteTask()
            finish()
        }
    }
}