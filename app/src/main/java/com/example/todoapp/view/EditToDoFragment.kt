package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todoapp.databinding.FragmentEditToDoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.DetailTodoViewModel

class EditToDoFragment : Fragment(), RadioClickListener, TodoEditClickListener  {
    private lateinit var binding: FragmentEditToDoBinding
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var todo: Todo
    override fun onCreateView(inflater: LayoutInflater, container:
    ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditToDoBinding.inflate(inflater,container,
            false)
        return binding.root
    }

    override fun onRadioClick(v: View, priority: Int, obj: Todo) {
        obj.priority = priority
    }
    override fun onTodoEditClick(v: View) {
        viewModel.update(binding.todo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioListener = this
        binding.saveListener = this

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.txtJudulToDo.text = "Edit Todo"
        binding.btnAdd.text = "Save Changes"

        val uuid = EditToDoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)

        //binding.btnAdd.setOnClickListener {
        //    val radio =
        //        view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
        //    viewModel.update(binding.txtTitle.text.toString(),
        //        binding.txtNotes.text.toString(), radio.tag.toString().toInt(), uuid)
        //    Toast.makeText(view.context, "Todo updated", Toast.LENGTH_SHORT).show()
        //    Navigation.findNavController(it).popBackStack()
        //}

        //cara dosen
        /*
        binding.btnAdd.setOnClickListener {
            todo.title = binding.txtTitle.text.toString()
            todo.notes = binding.txtNotes.text.toString()
            val radio =
                view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            todo.priority =  radio.tag.toString()
                .toInt()
            viewModel.update(todo)
            Toast.makeText(view.context, "Todo updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it).popBackStack()
        }
        //
        */
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            binding.todo = it

            /*
            //cara dosen
            todo = it
            //
            binding.txtTitle.setText(it.title)
            binding.txtNotes.setText(it.notes)

            when (it.priority) {
                1 -> binding.radioLow.isChecked = true
                2 -> binding.radioMedium.isChecked = true
                else -> binding.radioHigh.isChecked = true
            }*/

        })
    }




}