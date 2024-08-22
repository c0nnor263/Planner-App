package com.conboi.core.data.interfaces.dialog

import com.conboi.core.domain.enums.RepeatMode
import java.util.Calendar

interface ReminderDialogCallback {
    fun saveReminder(
        calendar: Calendar,
        bufferRepeatMode: RepeatMode,
    )

    fun removeReminder()
}
