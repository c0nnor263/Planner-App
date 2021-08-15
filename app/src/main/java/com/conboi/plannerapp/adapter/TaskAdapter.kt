package com.conboi.plannerapp.adapter

import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.conboi.plannerapp.data.TaskType
import com.conboi.plannerapp.databinding.ListTaskBinding
import com.conboi.plannerapp.ui.water.WaterFragmentDirections
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class TaskAdapter(
    private val listener: OnTaskClickListener
) : ListAdapter<TaskType, TaskAdapter.ViewHolder>(DiffCallback){

    inner class ViewHolder(private var binding: ListTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var timer: CountDownTimer? = null
        val nameTaskVh:TextInputEditText = binding.nameTask

        init {
            binding.apply {
                openTask.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onNameChanged(task, nameTask.text.toString())
                        val transitionName =
                            root.context.getString(com.conboi.plannerapp.R.string.edit_task_transition)
                        val extras = FragmentNavigatorExtras(root to transitionName)
                        val directions = WaterFragmentDirections.actionWaterFragmentToEditTask(
                            idTask = task.idTask
                        )
                        root.findNavController().navigate(directions, extras)
                    }
                }
                checkTask.setOnClickListener {
                    timer!!.cancel()
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task.copy(
                            nameTask = nameTaskVh.text.toString()
                        ), checkTask.isChecked)
                    }
                }
                nameTask.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        nameTaskLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    }
                    else{
                        nameTaskLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            }
        }

        fun bind(taskType: TaskType) = with(binding) {
            ViewCompat.setTransitionName(parentLayoutListTask, taskType.idTask.toString())

            nameTask.setText(taskType.nameTask)
            nameTaskLayout.isHintEnabled = false
            checkTask.isChecked = taskType.checkTask


            if (checkTask.isChecked) {
                nameTask.paint.isStrikeThruText = true
                nameTask.isEnabled = false
                nameTask.background.alpha = 255
                nameTask.setBackgroundColor(Color.TRANSPARENT)
                parentLayoutListTask.alpha = 0.5F
                parentLayoutListTask.setBackgroundResource(0)
            } else {
                nameTask.paint.isStrikeThruText = false
                nameTask.isEnabled = true
                parentLayoutListTask.alpha = 1.0F


                nameTask.setSelection(nameTask.length())
                nameTask.background.alpha = 255
                nameTask.setBackgroundColor(Color.TRANSPARENT)
                when (taskType.priorityTask) {
                    0 -> {
                        parentLayoutListTask.setBackgroundResource(com.conboi.plannerapp.R.drawable.fragment_water_gradient_priority_leisurely)
                    }
                    1 -> {
                        parentLayoutListTask.setBackgroundResource(com.conboi.plannerapp.R.drawable.fragment_water_gradient_priority_default)
                    }
                    2 -> {
                        parentLayoutListTask.setBackgroundResource(com.conboi.plannerapp.R.drawable.fragment_water_gradient_priority_advisable)
                    }
                    3 -> {
                        parentLayoutListTask.setBackgroundResource(com.conboi.plannerapp.R.drawable.fragment_water_gradient_priority_important)
                    }
                    else -> parentLayoutListTask.setBackgroundResource(com.conboi.plannerapp.R.drawable.fragment_water_gradient_priority_default)
                }
            }

            nameTask.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_PREVIOUS ||
                    actionId == EditorInfo.IME_ACTION_NONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                ) {
                    nameTask.setBackgroundColor(Color.TRANSPARENT)
                    nameTask.clearFocus()
                    listener.onNameChanged(taskType, nameTask.text.toString())

                }
                false
            }
            nameTask.doOnTextChanged { text, _, _, _ ->
                if (!taskType.checkTask) {
                    if (taskType.nameTask != text.toString()) {
                        timer!!.cancel()
                        timer!!.start()
                        nameTask.setBackgroundColor(Color.GRAY)
                        nameTask.background.alpha = 50
                    }
                }
            }
        }
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
        val adapterPosition = getItem(position)
        vh.bind(adapterPosition)

        //Very important code for some problems
        if (vh.timer != null) {
            vh.timer!!.cancel()
        }

        vh.timer = object : CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                vh.nameTaskVh.setBackgroundColor(Color.TRANSPARENT)
                vh.nameTaskVh.background.alpha = 255
                listener.onNameChanged(adapterPosition, vh.nameTaskVh.text.toString())
            }
        }
    }

    interface OnTaskClickListener {
        fun onCheckBoxClick(taskType: TaskType, isChecked: Boolean)
        fun onNameChanged(taskType: TaskType, name: String)
    }


    override fun getItemId(position: Int): Long = position.toLong()

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
}

