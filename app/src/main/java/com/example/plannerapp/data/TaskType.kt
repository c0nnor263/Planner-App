package com.example.plannerapp.data

import android.os.Parcelable
import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_CHECKED
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_CREATED
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_DESCRIPTION
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_NAME
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_PRIORITY
import com.example.plannerapp.data.TaskType.TaskEntry.COLUMN_TIME
import com.example.plannerapp.data.TaskType.TaskEntry.TABLE_NAME
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = TABLE_NAME)
@Parcelize
data class TaskType(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idTask") var idTask: Int = 0,
    @ColumnInfo(name = COLUMN_NAME) val nameTask: String?,
    @ColumnInfo(name = COLUMN_DESCRIPTION) val descriptionTask: String?,
    @ColumnInfo(name = COLUMN_TIME) val timeTask: Int,
    @ColumnInfo(name = COLUMN_PRIORITY) val priorityTask: Int = 1,
    @ColumnInfo(name = COLUMN_CHECKED) val checkTask: Boolean = false,
    @ColumnInfo(name = COLUMN_CREATED) val createdTask:Long = System.currentTimeMillis()
) :Parcelable{
    val createdTimeFormatted:String get() = DateFormat.getDateTimeInstance().format(createdTask)
    object TaskEntry:BaseColumns {
        const val COLUMN_NAME = "name_task"
        const val COLUMN_DESCRIPTION = "description_task"
        const val COLUMN_TIME = "due_time"
        const val COLUMN_PRIORITY = "priority_of_task"
        const val COLUMN_CHECKED = "isCheck"
        const val COLUMN_CREATED = "created"

        const val DATABASE_NAME = "TaskList.db"
        const val TABLE_NAME = "PTask"

    }
}