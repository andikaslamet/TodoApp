package com.example.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.example.todoapp.model.Todo

interface TodoCheckedChangeListener {
    fun onCheckedChanged(cb: CompoundButton,
                         isChecked:Boolean,
                         obj: Todo
    )
}

interface TodoEditClick {
    fun onTodoEditClick(v: View)
}

interface RadioClickListener {
    fun onRadioClick(v:View, priority:Int, obj:Todo)
}

interface TodoEditClickListener {
    fun onTodoEditClick(v: View)
}