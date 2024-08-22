package com.conboi.core.data.interfaces.dialog

import java.util.Calendar

interface DeadlineDialogCallback {
    fun saveDeadline(
        calendar: Calendar,
        missed: Boolean,
    )

    fun removeDeadline()
}
