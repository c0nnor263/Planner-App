package com.example.plannerapp.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.data.TaskType
import com.example.plannerapp.databinding.ListTaskBinding
import com.example.plannerapp.ui.water.WaterFragmentDirections
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class TaskAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<TaskType, TaskAdapter.ViewHolder>(DiffCallback) {
    var nameBuffer = ""
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TaskType>() {
            override fun areItemsTheSame(oldItem: TaskType, newItem: TaskType): Boolean {
                return oldItem.idTask == newItem.idTask
            }

            override fun areContentsTheSame(oldItem: TaskType, newItem: TaskType): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private var binding: ListTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                openTask.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                        val transitionName =
                            root.context.getString(com.example.plannerapp.R.string.edit_task_transition)
                        val extras = FragmentNavigatorExtras(root to transitionName)
                        val directions = WaterFragmentDirections.actionWaterFragmentToEditTask(
                            idTask = task.idTask
                        )
                        root.findNavController().navigate(directions, extras)
                    }
                }


                checkTask.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkTask.isChecked)
                    }
                }
                nameTask.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_PREVIOUS ||
                        actionId == EditorInfo.IME_ACTION_NONE ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                    ) {
                        nameTask.clearFocus()
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val task = getItem(position)
                            listener.onNameChanged(task, nameTask.text.toString())
                        }
                    }
                    false
                }
                nameTask.addTextChangedListener(
                    object : TextWatcher {
                        private var timer = Timer()
                        private val DELAY: Long = 1500
                        override fun onTextChanged(
                            s: CharSequence,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {

                        }

                        override fun beforeTextChanged(
                            s: CharSequence,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            nameBuffer = s.toString()
                            val position = adapterPosition
                            val task = getItem(position)
                            timer.cancel()
                            timer = Timer()
                            timer.schedule(
                                object : TimerTask() {
                                    override fun run() {
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onNameChanged(task, nameTask.text.toString())
                                        }
                                    }
                                },
                                DELAY
                            )
                            if (nameBuffer != task.nameTask) {
                                nameTask.setBackgroundColor(Color.GRAY)
                                nameTask.background.alpha = 50
                            }else {
                                nameTask.setBackgroundColor(Color.TRANSPARENT)
                                nameTask.setSelection(nameTask.length())
                            }
                        }
                    }
                )
                nameTask.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        nameTaskLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    }
                }
            }
        }

        fun bind(taskType: TaskType) {
            binding.apply {
                nameTask.setText(taskType.nameTask)
                nameTaskLayout.isHintEnabled = false
                if (taskType.checkTask) {
                    nameTask.paint.isStrikeThruText = true
                    nameTask.setTextColor(Color.GRAY)
                    //TODO("alpha")
                }
                checkTask.isChecked = taskType.checkTask
                ViewCompat.setTransitionName(parentLayoutListItem, taskType.idTask.toString())
                when (taskType.priorityTask) {
                    0 -> {
                        parentLayoutListItem.setBackgroundResource(com.example.plannerapp.R.drawable.gradient_priority_leisurely)
                    }
                    1 -> {
                        parentLayoutListItem.setBackgroundResource(com.example.plannerapp.R.drawable.gradient_priority_default)
                    }
                    2 -> {
                        parentLayoutListItem.setBackgroundResource(com.example.plannerapp.R.drawable.gradient_priority_advisable)
                    }
                    3 -> {
                        parentLayoutListItem.setBackgroundResource(com.example.plannerapp.R.drawable.gradient_priority_important)
                    }
                    else -> parentLayoutListItem.setBackgroundResource(com.example.plannerapp.R.drawable.gradient_priority_default)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(taskType: TaskType)
        fun onCheckBoxClick(taskType: TaskType, isChecked: Boolean)
        fun onNameChanged(taskType: TaskType, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position))
    }
}