package com.example.labmanager.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.Adapter.TestResultAdapter
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading.TestResultInteractor
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.R
import com.example.labmanager.Service.ItemClickedCallback
import kotlinx.android.synthetic.main.activity_add_test_result.*
import kotlinx.android.synthetic.main.dialog_filter_criteria.*
import kotlinx.android.synthetic.main.fragment_tests_results_overview.*
import java.util.*
import kotlin.collections.ArrayList


class TestsResultsOverviewFragment(context: Context) : Fragment(), DatePickerDialog.OnDateSetListener, ItemClickedCallback {

    var parentContext = context
    lateinit var filtersDialog: Dialog
    lateinit var datePickerDialog: DatePickerDialog
    var yearFrom = 0
    var monthFrom = 0
    var dayFrom = 0
    var stringDateFrom = ""
    var yearTo = 0
    var monthTo = 0
    var dayTo = 0
    var stringDateTo = ""

    var dateDialogEntry = 0
    var ENTRY_DATE_FROM = 1
    var ENTRY_DATE_TO = 2

    var resultsList = arrayListOf<UserTestResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tests_results_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_filter.setOnClickListener { showFiltersDialog() }

        TestResultInteractor(UserDataDBEntry).retrieveAllTestResultsForUser(::setUpRecycler, ::showErrorMessage)
    }

    fun setUpRecycler(results: ArrayList<UserTestResult>){
        resultsList = results
        val recyclerAdapter = TestResultAdapter(resultsList, parentContext, this)
        resltsRecyclerOverview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecyclerOverview.setHasFixedSize(true)
        resltsRecyclerOverview.adapter = recyclerAdapter
    }

    fun showErrorMessage(msg: String){}

    override fun itemAtPositionSelected(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    fun showFiltersDialog(){

        filtersDialog = Dialog(context)
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(R.layout.dialog_filter_criteria)
        val dateFromEditText = filtersDialog.findViewById(R.id.editTextDateFrom) as EditText
        val dateToEditText = filtersDialog.findViewById(R.id.editTextDateTo) as EditText
        dateFromEditText.setOnClickListener { setDateFrom() }
        dateToEditText.setOnClickListener { setDateTo() }

        val cancelButton = filtersDialog.findViewById(R.id.button_cancel) as Button
        val applyButton = filtersDialog.findViewById(R.id.button_apply) as Button

        cancelButton.setOnClickListener { filtersDialog.hide() }
        applyButton.setOnClickListener {
            applyFilters()
            filtersDialog.hide()
        }

    }

// ----------------------------- DATE -------------------------------------------------------------
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when(dateDialogEntry){
            ENTRY_DATE_FROM -> showDateFrom(year, month, dayOfMonth)
            ENTRY_DATE_TO -> showDateTo(year, month, dayOfMonth)
        }
    }

    fun setDateFrom() {
        dateDialogEntry = ENTRY_DATE_FROM
        datePickerDialog.show()
    }

    fun setDateTo() {
        dateDialogEntry = ENTRY_DATE_TO
        datePickerDialog.show()
    }


    private fun showDateFrom(year: Int, month: Int, day: Int) {
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        editTextDateFrom.setText(dateString)
        stringDateFrom = dateString
        yearFrom = year
        monthFrom = month
        dayFrom = day
    }

    private fun showDateTo(year: Int, month: Int, day: Int) {
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        editTextDateTo.setText(dateString)
        stringDateTo = dateString
        yearTo = year
        monthTo = month
        dayTo = day
    }


    fun applyFilters(){
        //TODO
    }

}
