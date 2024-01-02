package com.dicoding.todoapp.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dicoding.todoapp.R
import com.dicoding.todoapp.api.ApiConfig
import com.dicoding.todoapp.api.GetGeoResponse
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.utils.Event
import com.dicoding.todoapp.utils.TasksFilterType
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _getGeo = MutableLiveData<GetGeoResponse>()
    val getGeo : LiveData<GetGeoResponse> = _getGeo

    fun locate_now(){
        val getLocation = ApiConfig.getApiService().getGeo()
        getLocation.enqueue(object : Callback<GetGeoResponse>{
            override fun onResponse(
                call: Call<GetGeoResponse>,
                response: Response<GetGeoResponse>
            ) {
                if (response.isSuccessful){
                    _getGeo.value = response.body()
                }
            }

            override fun onFailure(call: Call<GetGeoResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private val _filter = MutableLiveData<TasksFilterType>()

    val tasks: LiveData<PagedList<Task>> = _filter.switchMap {
        taskRepository.getTasks(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    init {
        _filter.value = TasksFilterType.ALL_TASKS
    }

    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) {
            _snackbarText.value = Event(R.string.task_marked_complete)
        } else {
            _snackbarText.value = Event(R.string.task_marked_active)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }


}