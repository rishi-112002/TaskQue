package com.example.taskque.db

import android.util.Log
import androidx.room.Room
import com.example.taskque.App
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object DBManager {
    private const val TAG: String = "DB_MANAGER"
    private var dataBase: DataBase =
        Room.databaseBuilder(App.appContext, DataBase::class.java, "task_list").build()

    @OptIn(DelicateCoroutinesApi::class)
    fun insertData(
        title: String,
        date: String,
        time: String,
        content: String,
        side: String,
        taskType: String
    ) {
        val entity = RoomTaskData(title, date, time, content, side, taskType)
        GlobalScope.launch(Dispatchers.IO) {
            dataBase.taskQueDao().insertTask(entity)
            Log.d(TAG, entity.toString())
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateData(id:Int,
                   title: String,
                   date: String,
                   time: String,
                   content: String,
                   side: String,
                   taskType: String) {
        GlobalScope.launch (Dispatchers.IO){
            dataBase.taskQueDao().updateTask(id , title , date , time , content , side , taskType)
        }

    }
}



