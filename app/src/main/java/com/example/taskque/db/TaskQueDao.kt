package com.example.taskque.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskQueDao {
    @Query("select * from task_list")
    fun getAllTask(): LiveData<List<RoomTaskData>>

    @Insert
    suspend fun insertTask(roomTaskData: RoomTaskData)

    @Query("UPDATE task_list SET titleinputtext = :title, dateinput=:date , timeinput =:time ,  contentinput = :content ,side = :side , taskType = :taskType WHERE id = :Id")
    fun updateTask(
        Id: Int,
        title: String,
        date: String,
        time: String,
        content: String,
        side: String,
        taskType: String
    )

//    @Update
//    fun UpdateData(roomTaskData: RoomTaskData)

    @Query("DELETE from task_list")
    fun deleteAllTask()

    @Query("SELECT * FROM task_list WHERE id = :Id")
    fun getTaskById(Id: Int): LiveData<RoomTaskData>

    @Query("SELECT * FROM task_list WHERE tasktype = :taskType")
    fun getTaskByType(taskType: String): LiveData<List<RoomTaskData>>


}