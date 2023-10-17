package com.example.taskque.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.taskque.App
import com.example.taskque.R
import com.example.taskque.db.DataBase
import com.example.taskque.db.TaskQueDao
import com.example.taskque.utils.Constants.MyIntents.DATA_ITEM_EXTRA

class DetailedViewActivity : AppCompatActivity() {
    private lateinit var taskQueDao: TaskQueDao
    private lateinit var detailTaskType: TextView
    private lateinit var detailInputSide: TextView
    private lateinit var detailInputContent: TextView
    private lateinit var detailInputTime: TextView
    private lateinit var detailInputDate: TextView
    private lateinit var detailInputTitle: TextView
    private var itemId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_view_activity)
        val detailedToolbar = findViewById<Toolbar>(R.id.detailed_toolbar)
        setSupportActionBar(detailedToolbar)
        taskQueDao = Room.databaseBuilder(App.appContext, DataBase::class.java, "task_list").build()
            .taskQueDao()
        detailInputTitle = findViewById(R.id.detailedinputtitle)
        detailInputDate = findViewById(R.id.detailedinputdate)
        detailInputTime = findViewById(R.id.detailedinputtime)
        detailInputContent = findViewById(R.id.detailedinputcontent)
        detailInputSide = findViewById(R.id.detailedinputside)
        detailTaskType = findViewById(R.id.detailtasktype)
        itemId = intent.getIntExtra(DATA_ITEM_EXTRA, 0)

        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditViewActivity::class.java)
            intent.putExtra(DATA_ITEM_EXTRA, itemId)
            startActivity(intent)
        }
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        detailedToolbar.title = "TaskQue"
        detailedToolbar.subtitle = "detail's of your task "

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        taskQueDao.getTaskById(itemId).observe(this) {
            detailInputTitle.text = it.titleinputtext
            detailInputDate.text = it.dateinput
            detailInputTime.text = it.timeinput
            detailInputSide.text = it.side
            detailInputContent.text = it.contentinput
            detailTaskType.text = it.taskType
        }

    }
}