package com.example.taskque.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import com.example.taskque.R
import com.example.taskque.db.DataBase
import com.example.taskque.db.RoomTaskData
import com.example.taskque.db.TaskQueDao
import com.example.taskque.interfaces.DataItemClickListener
import com.example.taskque.ui.adapter.RecycleViewAdapter
import com.example.taskque.utils.Constants.MyIntents.DATA_ITEM_EXTRA
import com.example.taskque.utils.Constants.MyIntents.TASK_TYPE_ITEM_EXTRA
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomePageViewActivity : AppCompatActivity() {
    private lateinit var customAdapter: RecycleViewAdapter
    private lateinit var workRecycleView: RecyclerView
    private lateinit var taskDao: TaskQueDao

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homeview_activity)
        workRecycleView = findViewById(R.id.recycleOngoingTask)
        customAdapter = RecycleViewAdapter()
        workRecycleView.layoutManager = LinearLayoutManager(this)
        workRecycleView.adapter = customAdapter
        val button = findViewById<ImageView>(R.id.addButton)
        val workTaskButton = findViewById<ImageButton>(R.id.WorkTaskButton)
        val priorityTakButton = findViewById<ImageButton>(R.id.PriorityTaskButton)
        val routineTaskButton = findViewById<ImageButton>(R.id.RoutineTaskButton)
        val toolbar = findViewById<Toolbar>(R.id.taskToolbar)
//        val userImage = findViewById<ImageButton>(R.id.userImage)
        taskDao = databaseBuilder(this, DataBase::class.java, "task_list").build().taskQueDao()
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.reset -> {
                    GlobalScope.launch {
                        taskDao.deleteAllTask()
                    }
                    Toast.makeText(this, "  All entries are deleted", Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }

                R.id.exit -> {
                    finish()
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

        button.setOnClickListener {
            addTask()
        }
        workTaskButton.setOnClickListener {
            workTask()
        }

        priorityTakButton.setOnClickListener {
            priorityTask()
        }
        routineTaskButton.setOnClickListener {
            routineTask()
        }

    }

    private fun setData() {
        taskDao.getAllTask().observe(this) {
            customAdapter.setList(it)
        }
        customAdapter.setListener(object : DataItemClickListener {
            override fun onItemClicked(model: RoomTaskData) {
                val intent = Intent(this@HomePageViewActivity, DetailedViewActivity::class.java)
                intent.putExtra(DATA_ITEM_EXTRA, model.id)
                startActivity(intent)
            }
        }

        )
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun addTask() {
        val intent = Intent(this, AddTaskViewActivity::class.java)
        intent.putExtra(TASK_TYPE_ITEM_EXTRA, "")
        startActivity(intent)
    }

    private fun routineTask() {
        startActivity(Intent(this, RoutineTaskViewActivity::class.java))
    }

    private fun priorityTask() {
        val intent = Intent(
            this, PriorityTaskViewActivity::class.java
        )
        startActivity(intent)

    }

    private fun workTask() {
        val intent = Intent(
            this, WorkTasksViewActivity::class.java
        )
        startActivity(intent)
    }
}




