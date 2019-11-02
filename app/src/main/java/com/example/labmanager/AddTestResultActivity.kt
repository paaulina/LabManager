package com.example.labmanager

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_add_test_result.*
import android.text.TextWatcher
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.widget.DatePicker
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast




class AddTestResultActivity : AppCompatActivity() {

    lateinit var bloodTestsArray: ArrayList<BloodTest>
    lateinit var bloodTestAdapter: BloodTestAdapter
    var year = 0
    var month = 0
    var day = 0
    lateinit var selectedBloodTest : BloodTest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_test_result)

        var calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day)


        bloodTestsArray = DataBaseManager.getAllBloodTests()
        bloodTestAdapter = BloodTestAdapter(this, R.layout.blood_test_row, bloodTestsArray)
        autoCompleteTextView.threshold = 0
        autoCompleteTextView.setAdapter(bloodTestAdapter)

        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, l ->

            var selectedRow = parent.getItemAtPosition(i)
            selectedBloodTest = selectedRow as BloodTest
        }


        autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {0}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    progress_bar.visibility = View.VISIBLE
                } else {
                    progress_bar.visibility = View.INVISIBLE
                }
            }
        })
    }

    fun exitActivity(view: View){
        finish()
    }
    override fun onBackPressed(){}


    fun hideProggressBar(){
        progress_bar.visibility = View.INVISIBLE
    }

    override fun onCreateDialog(id: Int): Dialog? {
        return if (id == 999) {
            DatePickerDialog(
                this,
                myDateListener, year, month, day
            )
        } else null
    }

    fun setDate(view: View) {
        showDialog(999)
    }


    private val myDateListener = DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 ->
        showDate(arg1, arg2 + 1, arg3)
    }

    private fun showDate(year: Int, month: Int, day: Int) {
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }
        editTextDate.setText(dateString)
    }


}
