package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.Todo
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun fetch(uuid:Int) {
        launch {
            todoLD.postValue(buildDb(getApplication()).todoDao().selectTodo(uuid))
        }
    }
    fun addTodo(list: List<Todo>) {
        launch {
            val db = buildDb(getApplication())
            db.todoDao().insertAll(*list.toTypedArray())
        }
    }
    //fun update(title:String, notes:String, priority:Int, uuid:Int) {
    //    launch {
    //        val db = buildDb(getApplication())
    //        db.todoDao().update(title, notes, priority, uuid)
    //    }
    //}

    //cara dosen
    fun update(todo:Todo) {
        launch {
            buildDb(getApplication()).todoDao().updateTodo(todo)
        }
    }
    //
}
