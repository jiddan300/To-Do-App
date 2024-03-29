package com.dicoding.todoapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.todoapp.R
import com.dicoding.todoapp.api.ApiConfig
import com.dicoding.todoapp.api.GetGeoResponse
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.setting.SettingsActivity
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.add.AddTaskActivity
import com.dicoding.todoapp.utils.Event
import com.dicoding.todoapp.utils.TasksFilterType
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            val addIntent = Intent(this, AddTaskActivity::class.java)
            startActivity(addIntent)
        }

        recycler = findViewById(R.id.rv_task)
        recycler.layoutManager = LinearLayoutManager(this)

        initAction()

        val factory = ViewModelFactory.getInstance(this)
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        taskViewModel.tasks.observe(this, Observer(this::showRecyclerView))

        taskViewModel.snackbarText.observe(this){
            showSnackBar(it)
        }

        val fab_location = findViewById<ExtendedFloatingActionButton>(R.id.fab_location)
        fab_location.setOnClickListener {
            getGeo(fab_location)
        }
        getGeo(fab_location)
    }

    private fun getGeo(fab : ExtendedFloatingActionButton) {
        val getLocation = ApiConfig.getApiService().getGeo()
        getLocation.enqueue(object : Callback<GetGeoResponse> {
            override fun onResponse(
                call: Call<GetGeoResponse>,
                response: Response<GetGeoResponse>
            ) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        val location = "${body.countryCode}, ${body.country}, ${body.region}"
                        fab.text = location
                    }
                }
            }

            override fun onFailure(call: Call<GetGeoResponse>, t: Throwable) {
                fab.text = "Tap To Locate"
                Toast.makeText(this@TaskActivity, "Failed to get current location", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showRecyclerView(task: PagedList<Task>) {
        val adapter = TaskAdapter{ task, isDone ->
            taskViewModel.completeTask(task, isDone)
        }

        recycler.adapter = adapter
        adapter.submitList(task)
    }

    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getContentIfNotHandled() ?: return
        Snackbar.make(
            findViewById(R.id.coordinator_layout),
            getString(message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingIntent)
                true
            }
            R.id.action_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(R.id.action_filter) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                taskViewModel.filter(
                    when (it.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                true
            }
            show()
        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as TaskAdapter.TaskViewHolder).getTask
                taskViewModel.deleteTask(task)
            }

        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}