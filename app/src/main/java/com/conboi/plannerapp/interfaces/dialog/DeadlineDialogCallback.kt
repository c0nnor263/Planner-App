package com.conboi.plannerapp.interfaces.dialog

import java.util.Calendar

interface DeadlineDialogCallback {
    fun saveDeadline(
        calendar: Calendar,
        missed: Boolean,
    )

    fun removeDeadline()
}
