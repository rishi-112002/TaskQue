package com.example.taskque.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomTaskData::class] , version = 1 , exportSchema = false )
abstract class DataBase :RoomDatabase() {
    abstract  fun taskQueDao() : TaskQueDao

}