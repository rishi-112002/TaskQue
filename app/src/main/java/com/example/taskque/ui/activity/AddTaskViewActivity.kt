package com.example.taskque.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.taskque.R
import com.example.taskque.db.DBManager
import com.example.taskque.utils.Constants.MyIntents.TASK_TYPE_ITEM_EXTRA
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddTaskViewActivity : AppCompatActivity() {
    private var taskType: String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtaskview)
        val taskToolbar = findViewById<Toolbar>(R.id.addtaskheader)
        taskToolbar.title = "TaskQue"
        taskToolbar.subtitle = "add new task here"
        setSupportActionBar(taskToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
        val timeInput = findViewById<EditText>(R.id.timeInput)
        val dateInput = findViewById<EditText>(R.id.dateInput)
        val titleText = findViewById<TextInputEditText>(R.id.title_input_text)
        val contentInput = findViewById<EditText>(R.id.contentInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val dateText = findViewById<TextView>(R.id.textdate)
        val textTime = findViewById<TextView>(R.id.textTime)
        val timeButton = findViewById<ImageButton>(R.id.timebutton)
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        val typeRadioGroup = findViewById<RadioGroup>(R.id.radiotaskgroups)
        val radioWork = findViewById<RadioButton>(R.id.radiobtnwork)
        val radioPriority = findViewById<RadioButton>(R.id.radiobtnpriority)
        val radioRoutine = findViewById<RadioButton>(R.id.radiobtnRoutine)
        val togglesSideButton = findViewById<ToggleButton>(R.id.togglesidebtn)
        val categoriesLayout = findViewById<ConstraintLayout>(R.id.categoreiesLayout)
        taskType = intent.getStringExtra(TASK_TYPE_ITEM_EXTRA).toString()
        if (!TextUtils.isEmpty(taskType)) {
            categoriesLayout.visibility = View.INVISIBLE
        } else {
            categoriesLayout.visibility = View.VISIBLE
        }

        submitButton.setOnClickListener(View.OnClickListener {
            if (titleText.text!!.isEmpty() || contentInput.text.isEmpty() || dateInput.text.isEmpty() || timeInput.text.isEmpty()) {
                Toast.makeText(this, "Please fill all required Information ", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(taskType)) {
                val checkRadioBtn = typeRadioGroup.checkedRadioButtonId
                if (checkRadioBtn == -1) {
                    Toast.makeText(this, "please select  task ", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {

                    when (checkRadioBtn) {
                        R.id.radiobtnwork -> {
                            taskType = radioWork.text.toString()

                        }

                        R.id.radiobtnpriority -> {
                            taskType = radioPriority.text.toString()

                        }

                        R.id.radiobtnRoutine -> {
                            taskType = radioRoutine.text.toString()
                        }

                        else -> {
                            return@OnClickListener
                        }
                    }
                }
            }
            val toggleSide = if (togglesSideButton.isChecked) "Urgent" else "Non-Urgent"
            val title = titleText.text.toString()
            val date = dateInput.text.toString()
            val time = timeInput.text.toString()
            val content = contentInput.text.toString()
            val addTaskType = taskType
            DBManager.insertData(title, date, time, content, toggleSide, addTaskType)
            finish()
        })


        dateText.setOnClickListener {
            showDatePicker()
        }

        calendarButton.setOnClickListener {
            showDatePicker()
        }


        timeButton.setOnClickListener {
            showTimePicker()
        }

        textTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val date: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH)
        val year: Int = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDate ->
                val selectDate = "$selectedDate/${selectedMonth + 1}/$selectedYear"
                updateDate(selectDate)
            },
            date, month, year
        )
        datePickerDialog.show()

    }

    private fun updateDate(data: String) {
        val dateText = findViewById<EditText>(R.id.dateInput)
        dateText.setText(data).toString()
        dateText.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hh: Int = calendar.get(Calendar.HOUR)
        val mm: Int = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this, { _, selectHour, selectMinute ->
                val selectTime = "$selectHour:$selectMinute"
                updateTime(selectTime)

            },
            hh, mm, false
        )
        timePickerDialog.show()
    }

    private fun updateTime(time: String) {
        val timeInput = findViewById<EditText>(R.id.timeInput)
        timeInput.setText(time).toString()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}


