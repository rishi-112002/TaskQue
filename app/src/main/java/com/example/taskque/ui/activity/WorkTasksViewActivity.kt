package com.example.taskque.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.taskque.App
import com.example.taskque.R
import com.example.taskque.db.DataBase
import com.example.taskque.db.RoomTaskData
import com.example.taskque.db.TaskQueDao
import com.example.taskque.interfaces.DataItemClickListener
import com.example.taskque.ui.adapter.RecycleViewAdapter
import com.example.taskque.utils.Constants
import com.example.taskque.utils.Constants.MyIntents.TASK_TYPE_ITEM_EXTRA
import com.example.taskque.utils.Constants.MyIntents.taskWork

class WorkTasksViewActivity : AppCompatActivity() {


    private lateinit var workAdapter: RecycleViewAdapter
    private lateinit var workRecycleView: RecyclerView
    private lateinit var taskQueDao: TaskQueDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_tasks_view)
        workRecycleView = findViewById(R.id.workRecycleview)
        workAdapter = RecycleViewAdapter()
        workRecycleView.layoutManager = LinearLayoutManager(this)
        workRecycleView.adapter = workAdapter
        taskQueDao = Room.databaseBuilder(App.appContext, DataBase::class.java, "task_list").build()
            .taskQueDao()
        val toolbar = findViewById<Toolbar>(R.id.worktaskheader)
        toolbar.title = " Work Task"
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val workButton : ImageView = findViewById(R.id.workaddbutton)
        workButton.setOnClickListener {
            val intent = Intent(this, AddTaskViewActivity::class.java)
            intent.putExtra(TASK_TYPE_ITEM_EXTRA, taskWork)
            startActivity(intent)
        }
        setData()
    }

    private fun setData() {
        taskQueDao.getTaskByType(taskWork).observe(this) {
            workAdapter.setList(it)
        }
        workAdapter.setListener(object : DataItemClickListener {
            override fun onItemClicked(model: RoomTaskData) {
                val intent = Intent(this@WorkTasksViewActivity, DetailedViewActivity::class.java)
                intent.putExtra(Constants.MyIntents.DATA_ITEM_EXTRA, model.id)
                startActivity(intent)
            }
        }

        )

    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}