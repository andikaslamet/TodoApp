package com.example.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todoapp.databinding.FragmentCreateTodoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.util.NotificationHelper
import com.example.todoapp.util.TodoWorker
import com.example.todoapp.viewmodel.DetailTodoViewModel
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment(),RadioClickListener,TodoEditClickListener {
    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    override fun onRadioClick(v: View, priority: Int, obj: Todo) {
        obj.priority = priority
    }
    override fun onTodoEditClick(v: View) {
        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                ))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

        val radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
        var todo = Todo(
            binding.txtTitle.text.toString(),
            binding.txtNotes.text.toString(),
            radio?.tag.toString().toInt()
        )
        val list = listOf(todo)
        viewModel.addTodo(list)
        Toast.makeText(view?.context, "Data added", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), NotificationHelper.REQUEST_NOTIF)
        }

        viewModel =
            ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.todo = Todo("","",3)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.addListener = this
        binding.radioListener = this
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode ==NotificationHelper.REQUEST_NOTIF) {
            if(grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                NotificationHelper(requireContext())
                    .createNotification("Todo Created",
                        "A new todo has been created! Stay focus!")
            }
        }
    }
}