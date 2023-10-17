package com.example.taskque

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.taskque.db.DataBase

class App : Application() {
    companion object {
        lateinit var appContext: Context // Global context variable
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext // Initialize the global context
        Room.databaseBuilder(this, DataBase::class.java, "task_list").build()

    }

}