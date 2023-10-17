package com.example.taskque.interfaces

import com.example.taskque.db.RoomTaskData

interface DataItemClickListener {
    fun onItemClicked(model: RoomTaskData)
}
