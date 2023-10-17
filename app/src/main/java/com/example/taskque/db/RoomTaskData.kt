package com.example.taskque.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_list")
data class RoomTaskData(
    val titleinputtext: String,
    val dateinput: String,
    var timeinput: String,
    val contentinput: String,
    val side: String,
    val taskType: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
