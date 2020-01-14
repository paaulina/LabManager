package com.example.labmanager.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.*
import com.example.labmanager.adapter.TestResultAdapter
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.DateManager
import com.example.labmanager.service.ItemClickedCallback
import com.example.labmanager.service.TestResultsFilter
import kotlinx.android.synthetic.main.fragment_tests_results_overview.*
import kotlin.collections.ArrayList
import android.view.*
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsSavingCallback
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsPresenter
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestsResultSavingPresenter
import com.example.labmanager.dialog.DetailsDialogDisplayer
import com.example.labmanager.dialog.DialogManager
import com.example.labmanager.service.UserTestsResultsGroupManager


class TestsResultsOverviewFragment(
    context: Context
) : Fragment(),
    DatePickerDialog.OnDateSetListener,
    ItemClickedCallback,
    TestResultsPresenter,
    TestResultsSavingCallback,
    TestsResultSavingPresenter,
    DetailsDialogDisplayer{


    private var parentContext = context
    private lateinit var filtersDialog: Dialog
    private lateinit var datePickerDialog: DatePickerDialog
    private var stringDateFrom = ""
    private var stringDateTo = ""

    private var dateDialogEntry = 0
    private var entryDateFrom = 1
    private var entryDateTo = 2

    private var resultsList = arrayListOf<UserTestResult>()
    private var displayableResultList = arrayListOf<UserTestResult>()
    private var showingOnlyFavs = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tests_results_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_filter.setOnClickListener {
            showFiltersDialog()
        }

        resltsRecyclerOverview.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE

        buttonFavFilter.setOnClickListener {
            if(showingOnlyFavs){
                showingOnlyFavs = false
                applyFilters()
            } else {
                showingOnlyFavs = true
                showOnlyFavourites()
            }
        }
        TestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }


    override fun presentUsersTestResults(testResults: ArrayList<UserTestResult>) {

        if(resltsRecyclerOverview != null) {
            resltsRecyclerOverview.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
            buttonFavFilter.visibility = View.VISIBLE
            button_filter.visibility = View.VISIBLE
            resultsList = UserTestsResultsGroupManager(arrayListOf()).sortByDate(testResults)
            setUpRecycler(resultsList)
            textViewNoItems.visibility = View.GONE
            applyFilters()
        }
    }




    override fun presentRetrievalError() {
        if(progress_bar != null && textViewNoItems !=null){
            progress_bar.visibility = View.GONE
            textViewNoItems.visibility = View.VISIBLE
        }
    }


    private fun showOnlyFavourites(){
        if(displayableResultList.isNotEmpty()){
            buttonFavFilter.setImageDrawable(resources.getDrawable(R.drawable.star))
            val favItems = arrayListOf<UserTestResult>()
            for(res in displayableResultList){
                if(res.favorite == IS_FAVORITE){
                    favItems.add(res)
                }
            }
            displayableResultList = favItems
            setUpRecycler(displayableResultList)
        }

    }


    fun setUpRecycler(results: ArrayList<UserTestResult>){

        resltsRecyclerOverview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecyclerOverview.setHasFixedSize(true)
        displayableResultList = results
        var recyclerAdapter = TestResultAdapter(displayableResultList, parentContext, this)
        resltsRecyclerOverview.adapter = recyclerAdapter
        recyclerAdapter.notifyDataSetChanged()

        if(textViewNoItems !=null){
            if(displayableResultList.isEmpty()){
                textViewNoItems.visibility = View.VISIBLE
            } else {
                textViewNoItems.visibility = View.GONE
            }
        }

    }



//################################## FILTERS DIALOG ##################################################

    private lateinit var filterNameEditText : EditText
    private lateinit var dateFromEditText : EditText
    private lateinit var dateToEditText : EditText
    private var filterName : String = ""
    private var dateFromSet = false
    private var dateToSet = false
    private lateinit var sortingSpinner : Spinner

    private fun showFiltersDialog(){
        dateFromSet = false
        dateToSet = false
        filtersDialog = Dialog(context)
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(R.layout.dialog_filter_criteria)
        dateFromEditText = filtersDialog.findViewById(R.id.editTextDateFrom) as EditText
        dateToEditText = filtersDialog.findViewById(R.id.editTextDateTo) as EditText
        filterNameEditText = filtersDialog.findViewById(R.id.autoCompleteTextView) as AutoCompleteTextView
        dateFromEditText.setOnClickListener {
            dateFromEditText.setText("")
            stringDateFrom = ""
            dateFromSet = false
            setDateFrom()
        }
        dateToEditText.setOnClickListener {
            dateToEditText.setText("")
            stringDateTo =""
            dateToSet = false
            setDateTo()
        }


        sortingSpinner = filtersDialog.findViewById(R.id.spinner_sorting)
        setUpSortingSpinner()
        setUpFiltersFields()

        sortingSpinner.setSelection(1)

        val cancelButton = filtersDialog.findViewById(R.id.button_cancel) as Button
        val applyButton = filtersDialog.findViewById(R.id.button_apply) as Button

        cancelButton.setOnClickListener { filtersDialog.hide() }
        applyButton.setOnClickListener {
            if(checkDatesRange()){
                filterName = filterNameEditText.text.toString()
                applyFilters()
                filtersDialog.hide()
            }
        }

        datePickerDialog = DatePickerDialog(parentContext)
        datePickerDialog.setOnDateSetListener(this)

        filtersDialog.show()
        setUpFiltersFields()

    }

    private fun checkDatesRange() : Boolean{
        if(dateFromEditText.text.isNotEmpty() && dateToEditText.text.isNotEmpty()){
            if( DateManager.toMillis(stringDateFrom) > DateManager.toMillis(stringDateTo)){
                dateFromEditText.setTextColor(Color.RED)
                dateToEditText.setTextColor(Color.RED)
                return false
            }
        } else{
            dateFromEditText.setTextColor(resources.getColor(R.color.colorPrimary))
            dateToEditText.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        }
        return true
    }

    private fun setUpFiltersFields(){
        filterNameEditText.setText(filterName)
        dateFromEditText.setText(stringDateFrom)
        dateToEditText.setText(stringDateTo)

        if(selectedSorting.isNotEmpty()){
            when(selectedSorting){
                sortingTypesArray.get(0) -> sortingSpinner.setSelection(0)
                sortingTypesArray.get(1) -> sortingSpinner.setSelection(1)

            }
        }
    }

    // ----------------------------- DATE -------------------------------------------------------------
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when(dateDialogEntry){
            entryDateFrom -> showDateFrom(year, month+1, dayOfMonth)
            entryDateTo -> showDateTo(year, month+1, dayOfMonth)
        }
    }

    private fun setDateFrom() {
        dateDialogEntry = entryDateFrom
        datePickerDialog.show()
    }

    private fun setDateTo() {
        dateDialogEntry = entryDateTo
        datePickerDialog.show()
    }


    private fun showDateFrom(year: Int, month: Int, day: Int) {
        var monthString = if(month < 10){
            "0$month"
        }else{
            "$month"
        }
        var dateString = "$day/$monthString/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        dateFromEditText.setText(dateString)
        dateFromSet = true
        stringDateFrom = dateString
    }

    private fun showDateTo(year: Int, month: Int, day: Int) {

        var monthString = if(month < 10){
            "0$month"
        }else{
            "$month"
        }
        var dateString = "$day/$monthString/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        dateToEditText.setText(dateString)
        dateToSet = true
        stringDateTo = dateString
        Log.d("dateToSetting " , " date to setting $dateString")
    }

    private var sortingTypesInitialized = false
    private lateinit var sortingTypesArray : Array<String>
    private var selectedSorting = ""
    private fun setUpSortingSpinner(){

        sortingTypesArray = resources.getStringArray(R.array.sorting_types_array)
        sortingTypesInitialized = true
        val typesAdapter = ArrayAdapter<String>(parentContext,
            R.layout.simple_spinner_item,
            sortingTypesArray
        )

        sortingSpinner.adapter = typesAdapter
        sortingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                selectedSorting = sortingTypesArray.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSorting = ""
            }
        }
    }


    private fun applyFilters(){

        Log.d("DateLog" , "datelog $stringDateFrom $stringDateTo")
        buttonFavFilter.setImageDrawable(resources.getDrawable(R.drawable.prestar))
        displayableResultList = resultsList
        if(filterName.length > 0){
            displayableResultList = TestResultsFilter.filterByName(filterName, displayableResultList)
        }

        if(stringDateFrom.isNotEmpty()){
            displayableResultList = TestResultsFilter.filterByDateFrom(DateManager.toMillis(stringDateFrom), displayableResultList)
        }

        if(stringDateTo.isNotEmpty()){
            displayableResultList = TestResultsFilter.filterByDateTo(DateManager.toMillis(stringDateTo), displayableResultList)
        }
        if(sortingTypesInitialized){
            when(selectedSorting){
                sortingTypesArray.get(0) -> displayableResultList = TestResultsFilter.sortByName(displayableResultList)
                sortingTypesArray.get(1) -> displayableResultList = TestResultsFilter.sortByDate(displayableResultList)

            }
        }
        setUpRecycler(displayableResultList)
    }

// ----------------------------------------- item selected ----------------------------------------------------------------

    private lateinit var selectedResult : UserTestResult
    override fun itemAtPositionSelected(position: Int) {
        selectedResult = displayableResultList[position]
        DialogManager().showDetailsDialog(DIALOG_EDITABLE, displayableResultList[position], parentContext, this)
    }
    override fun itemAtPositionLongClicked(position: Int) {}


    override fun addResultToFavourites() {
        selectedResult.favorite = IS_FAVORITE
        TestResultsInteractor(UserDataDBEntry).setFavourite(selectedResult, this)
    }

    override fun removeResultFromFavourites() {
        selectedResult.favorite = NOT_FAVORITE
        TestResultsInteractor(UserDataDBEntry).setUNFavourite(selectedResult, this)
    }

    override fun deleteSelectedResult() {
        TestResultsInteractor(UserDataDBEntry).deleteUserTestResult(selectedResult,this)
    }

    override fun presentSaveSuccess() {}

    override fun presentSaveError() {}

    override fun onSaveSuccess() {
        TestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }

    override fun onSaveFailure() {}

}
