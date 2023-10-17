package com.example.taskque.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.taskque.R
import com.example.taskque.db.DataBase
import com.example.taskque.db.RoomTaskData
import com.example.taskque.db.TaskQueDao
import com.example.taskque.interfaces.DataItemClickListener
import com.example.taskque.ui.adapter.RecycleViewAdapter
import com.example.taskque.utils.Constants.MyIntents.DATA_ITEM_EXTRA
import com.example.taskque.utils.Constants.MyIntents.TASK_TYPE_ITEM_EXTRA
import com.example.taskque.utils.Constants.MyIntents.taskPriority
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PriorityTaskViewActivity : AppCompatActivity() {
    private lateinit var customAdapter: RecycleViewAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var taskQueDao: TaskQueDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.priority_task_view)
        taskQueDao =
            Room.databaseBuilder(this, DataBase::class.java, "task_list").build().taskQueDao()
        recycleView = findViewById(R.id.Priority_task_recycleview)
        customAdapter = RecycleViewAdapter()
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = customAdapter
        val toolbar = findViewById<Toolbar>(R.id.toolbar_header_priority_task)
        toolbar.title = " Priority Task"
        setSupportActionBar(toolbar)
        val priorityButton = findViewById<ImageView>(R.id.priorityaddbutton)
        priorityButton.setOnClickListener {
            val intent = Intent(this, AddTaskViewActivity::class.java)
            intent.putExtra(TASK_TYPE_ITEM_EXTRA, taskPriority)
            startActivity(intent)
        }
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)

        }
        setData()
    }

    private fun setData() {
        taskQueDao.getTaskByType(taskPriority).observe(this) {
            customAdapter.setList(it)
        }
        customAdapter.setListener(object : DataItemClickListener {
            override fun onItemClicked(model: RoomTaskData) {
                val intent = Intent(this@PriorityTaskViewActivity, DetailedViewActivity::class.java)
                intent.putExtra(DATA_ITEM_EXTRA, model.id)
                startActivity(intent)
            }
        }

        )

    }

    override fun onResume() {
        setData()
        super.onResume()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}