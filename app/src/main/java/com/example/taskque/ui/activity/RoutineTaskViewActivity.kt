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
import com.example.taskque.utils.Constants.MyIntents.taskRoutine
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RoutineTaskViewActivity : AppCompatActivity() {

    private lateinit var taskQueDao: TaskQueDao
    private lateinit var customAdapter: RecycleViewAdapter
    private lateinit var recycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routine_task_view)
        recycleView = findViewById(R.id.routineRecycleview)
        customAdapter = RecycleViewAdapter()
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = customAdapter
        taskQueDao =
            Room.databaseBuilder(this, DataBase::class.java, "task_list").build().taskQueDao()
        val toolbar = findViewById<Toolbar>(R.id.routinetaskheader)
        setSupportActionBar(toolbar)
        toolbar.title = " Routine Task"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val routineButton = findViewById<ImageView>(R.id.routineaddbutton)
        routineButton.setOnClickListener {
            val intent = Intent(this, AddTaskViewActivity::class.java)
            intent.putExtra(TASK_TYPE_ITEM_EXTRA, taskRoutine)
            startActivity(intent)

        }
        setData()
    }

    private fun setData() {

        taskQueDao.getTaskByType(taskRoutine).observe(this) {
            customAdapter.setList(it)
        }
        customAdapter.setListener(object : DataItemClickListener {
            override fun onItemClicked(model: RoomTaskData) {
                val intent = Intent(this@RoutineTaskViewActivity, DetailedViewActivity::class.java)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}