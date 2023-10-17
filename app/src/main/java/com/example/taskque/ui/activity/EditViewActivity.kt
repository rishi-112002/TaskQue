package com.example.taskque.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.taskque.App
import com.example.taskque.R
import com.example.taskque.db.DBManager
import com.example.taskque.db.DataBase
import com.example.taskque.db.TaskQueDao
import com.example.taskque.utils.Constants.MyIntents.DATA_ITEM_EXTRA
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class EditViewActivity : AppCompatActivity() {
    private var itemId: Int = 0
    private lateinit var taskQueDao: TaskQueDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_view)
        taskQueDao = Room.databaseBuilder(App.appContext, DataBase::class.java, "task_list").build()
            .taskQueDao()
        val titleInputText = findViewById<TextInputEditText>(R.id.titleInputtext)
        val contentInputText = findViewById<TextInputEditText>(R.id.contentinputtext)
        val dateText = findViewById<MaterialAutoCompleteTextView>(R.id.dateinputtext)
        val timeText = findViewById<MaterialAutoCompleteTextView>(R.id.timeinputtext)
        val saveButton = findViewById<Button>(R.id.savebutton)
        val sideButton = findViewById<ToggleButton>(R.id.sidebutton)
        val editToolbar = findViewById<Toolbar>(R.id.editheaderbar)
        val taskSpinner = findViewById<Spinner>(R.id.tasktypespinner)
        val item = arrayOf("Work", "Priority", "Routine")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_layout, item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskSpinner.adapter = adapter
        editToolbar.title = " Edit View"
        editToolbar.subtitle = "you can recreate your task here"
        setSupportActionBar(editToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        dateText.setOnClickListener {
            showDatePicker()
        }
        timeText.setOnClickListener {
            showTimePicker()
        }

        itemId = intent.getIntExtra(DATA_ITEM_EXTRA, 0)
        taskQueDao.getTaskById(itemId).observe(this) {
            it.contentinput
            contentInputText.setText(it.contentinput)
            titleInputText.setText(it.titleinputtext)
            dateText.setText(it.dateinput)
            timeText.setText(it.timeinput)
            sideButton.text = it.side
        }
        var selectedItem = ""
        taskSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    selectedItem = parentView!!.getItemAtPosition(p2).toString()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(
                        this@EditViewActivity,
                        "please select type",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        saveButton.setOnClickListener(View.OnClickListener {

            if (timeText.text!!.isEmpty() || dateText.text!!.isEmpty() || titleInputText.text!!.isEmpty() || contentInputText.text!!.isEmpty()) {
                Toast.makeText(this, "Please fill required information", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val editTitle = titleInputText.text.toString()
                val editDate = dateText.text.toString()
                val editTime = timeText.text.toString()
                val editContent = contentInputText.text.toString()
                val editSide = if (sideButton.isChecked) "Non-Urgent" else "Urgent"
                val taskType = selectedItem

                DBManager.updateData(
                    itemId,
                    editTitle,
                    editDate,
                    editTime,
                    editContent,
                    editSide,
                    taskType
                )
                finish()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDatePicker() {

        val calendar: Calendar = Calendar.getInstance()
        val date: Int = calendar.get(Calendar.DATE)
        val month: Int = calendar.get(Calendar.MONTH)
        val year: Int = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDate ->
                val selectDate = "${selectedDate}/${selectedMonth + 1}/${selectedYear}"
                updateDate(selectDate)
            },
            date,
            month,
            year
        )
        datePickerDialog.show()


    }

    private fun updateDate(data: String) {
        val dateInputText = findViewById<MaterialAutoCompleteTextView>(R.id.dateinputtext)
        dateInputText.setText(data)

    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hh: Int = calendar.get(Calendar.HOUR)
        val mm: Int = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this, { _, selectHour, selectMinute ->
                val selective = "$selectHour:$selectMinute"
                updateTime(selective)

            },
            hh, mm, false
        )
        timePickerDialog.show()
    }

    private fun updateTime(time: String) {
        val timeInput = findViewById<MaterialAutoCompleteTextView>(R.id.timeinputtext)
        timeInput.setText(time)

    }
}
