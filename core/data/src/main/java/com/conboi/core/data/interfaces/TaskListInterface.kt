package com.conboi.core.data.interfaces

import com.conboi.core.data.model.TaskType
import com.conboi.plannerapp.interfaces.ListInterface

interface TaskListInterface : ListInterface {
    fun onCheckBoxEvent(
        task: TaskType,
        isChecked: Boolean,
        isHold: Boolean,
    )

    fun onTitleChanged(
        task: TaskType,
        title: String,
    )

    fun showPremiumDealDialog()
}
