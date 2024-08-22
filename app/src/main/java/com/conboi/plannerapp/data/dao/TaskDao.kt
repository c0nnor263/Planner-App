package com.conboi.plannerapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.conboi.core.data.model.TaskType
import com.conboi.core.data.model.TaskType.Companion.COLUMN_CHECKED
import com.conboi.core.data.model.TaskType.Companion.COLUMN_TOTAL_CHECKED
import com.conboi.core.data.model.TaskType.Companion.TABLE_NAME
import com.conboi.core.domain.enums.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    fun getSortedTasks(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
        hideOvercompleted: Boolean,
    ): Flow<List<TaskType>> =
        when (sortOrder) {
            SortOrder.BY_TITLE ->
                getTasksSortByName(
                    searchQuery,
                    hideCompleted,
                    hideOvercompleted,
                )

            SortOrder.BY_DATE ->
                getTasksSortByCreatedDate(
                    searchQuery,
                    hideCompleted,
                    hideOvercompleted,
                )

            SortOrder.BY_COMPLETE ->
                getTasksSortByCompletedDate(
                    searchQuery,
                    hideCompleted,
                    hideOvercompleted,
                )

            SortOrder.BY_OVERCOMPLETED ->
                getTasksSortByOvercompleted(
                    searchQuery,
                    hideCompleted,
                    hideOvercompleted,
                )
        }

    @Query(
        "SELECT * FROM $TABLE_NAME WHERE (:hideOvercompleted != (totalChecked > 1) or $COLUMN_TOTAL_CHECKED < 2) AND (:hideCompleted != checked or (checked = 0 or totalChecked > 1)) AND title LIKE '%' || :searchQuery || '%' ORDER BY priority DESC, title",
    )
    fun getTasksSortByName(
        searchQuery: String,
        hideCompleted: Boolean,
        hideOvercompleted: Boolean,
    ): Flow<List<TaskType>>

    @Query(
        "SELECT * FROM $TABLE_NAME WHERE (:hideOvercompleted != (totalChecked > 1) or $COLUMN_TOTAL_CHECKED < 2) AND (:hideCompleted != checked or (checked = 0 or totalChecked > 1)) AND title LIKE '%' || :searchQuery || '%' ORDER BY priority DESC, created DESC",
    )
    fun getTasksSortByCreatedDate(
        searchQuery: String,
        hideCompleted: Boolean,
        hideOvercompleted: Boolean,
    ): Flow<List<TaskType>>

    @Query(
        "SELECT * FROM $TABLE_NAME WHERE (:hideOvercompleted != (totalChecked > 1) or $COLUMN_TOTAL_CHECKED < 2) AND (:hideCompleted != checked or (checked = 0 or totalChecked > 1)) AND title LIKE '%' || :searchQuery || '%' ORDER BY completed, priority DESC",
    )
    fun getTasksSortByCompletedDate(
        searchQuery: String,
        hideCompleted: Boolean,
        hideOvercompleted: Boolean,
    ): Flow<List<TaskType>>

    @Query(
        "SELECT * FROM $TABLE_NAME WHERE (:hideOvercompleted != (totalChecked > 1) or $COLUMN_TOTAL_CHECKED < 2) AND (:hideCompleted != checked or (checked = 0 or totalChecked > 1)) AND title LIKE '%' || :searchQuery || '%' ORDER BY totalChecked DESC, priority DESC, title",
    )
    fun getTasksSortByOvercompleted(
        searchQuery: String,
        hideCompleted: Boolean,
        hideOvercompleted: Boolean,
    ): Flow<List<TaskType>>

    @Query("SELECT * from $TABLE_NAME WHERE id == :id")
    fun getTask(id: Int): Flow<TaskType>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllTasks(): LiveData<List<TaskType>>

    @Query("SELECT * from $TABLE_NAME WHERE $COLUMN_CHECKED == 1 AND $COLUMN_TOTAL_CHECKED < 2")
    fun getCompletedTasks(): Flow<List<TaskType>>

    @Query("SELECT * from $TABLE_NAME WHERE $COLUMN_TOTAL_CHECKED > 1")
    fun getOvercompletedTasks(): Flow<List<TaskType>>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME")
    fun getTaskSize(): LiveData<Int>

    @Query("SELECT COUNT(*) from $TABLE_NAME WHERE $COLUMN_CHECKED == 1 AND $COLUMN_TOTAL_CHECKED < 2")
    fun getCompletedTaskSize(): LiveData<Int>

    @Query("SELECT COUNT(*) from $TABLE_NAME WHERE $COLUMN_TOTAL_CHECKED > 1")
    fun getOvercompletedTaskSize(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(taskType: TaskType)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(taskType: List<TaskType>)

    @Update
    suspend fun update(taskType: TaskType)

    @Delete
    suspend fun delete(taskType: TaskType)

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_CHECKED == 1 AND $COLUMN_TOTAL_CHECKED < 2")
    suspend fun deleteCompletedTasks()

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_TOTAL_CHECKED > 1")
    suspend fun deleteOvercompletedTasks()

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllTasks()
}
