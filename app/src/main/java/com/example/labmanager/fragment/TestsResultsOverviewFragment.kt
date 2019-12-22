package com.example.labmanager.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import com.example.labmanager.service.UserTestsResultsGroupManager


class TestsResultsOverviewFragment(
    context: Context
) : Fragment(),
    DatePickerDialog.OnDateSetListener,
    ItemClickedCallback,
    TestResultsPresenter,
    TestResultsSavingCallback,
    TestsResultSavingPresenter{


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
        }
    }




    override fun presentRetrievalError() {
        progress_bar.visibility = View.GONE
    }


    private fun showOnlyFavourites(){

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


    private fun setUpRecycler(results: ArrayList<UserTestResult>){
        displayableResultList = results
        val recyclerAdapter = TestResultAdapter(displayableResultList, parentContext, this)
        resltsRecyclerOverview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecyclerOverview.setHasFixedSize(true)
        resltsRecyclerOverview.adapter = recyclerAdapter
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

        val cancelButton = filtersDialog.findViewById(R.id.button_cancel) as Button
        val applyButton = filtersDialog.findViewById(R.id.button_apply) as Button

        cancelButton.setOnClickListener { filtersDialog.hide() }
        applyButton.setOnClickListener {
            filterName = filterNameEditText.text.toString()
            applyFilters()
            filtersDialog.hide()
        }

        datePickerDialog = DatePickerDialog(parentContext)
        datePickerDialog.setOnDateSetListener(this)

        filtersDialog.show()
        setUpFiltersFields()

    }

    private fun setUpFiltersFields(){
        filterNameEditText.setText(filterName)
        dateFromEditText.setText(stringDateFrom)
        dateToEditText.setText(stringDateTo)

        if(selectedSorting.isNotEmpty()){
            when(selectedSorting){
                sortingTypesArray[0] -> sortingSpinner.setSelection(0)
                sortingTypesArray[1] -> sortingSpinner.setSelection(1)

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
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        dateFromEditText.setText(dateString)
        dateFromSet = true
        stringDateFrom = dateString
    }

    private fun showDateTo(year: Int, month: Int, day: Int) {
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        dateToEditText.setText(dateString)
        dateToSet = true
        stringDateTo = dateString
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
        buttonFavFilter.setImageDrawable(resources.getDrawable(R.drawable.prestar))
        displayableResultList = resultsList
        if(filterName.isNotEmpty()){
            displayableResultList = TestResultsFilter.filterByName(filterName, displayableResultList)
        }

        if(dateFromEditText.text.isNotEmpty()){
            displayableResultList = TestResultsFilter.filterByDateFrom(DateManager.toMillis(dateFromEditText.text.toString()),
                                                                       displayableResultList)
            stringDateFrom = dateFromEditText.text.toString()
        }

        if(dateToEditText.text.isNotEmpty()){
            displayableResultList = TestResultsFilter.filterByDateTo(DateManager.toMillis(dateToEditText.text.toString()),
                                                                     displayableResultList)
            stringDateTo = dateToEditText.text.toString()
        }
        if(sortingTypesInitialized){
            when(selectedSorting){
                sortingTypesArray[0] -> displayableResultList = TestResultsFilter.sortByName(displayableResultList)
                sortingTypesArray[1] -> displayableResultList = TestResultsFilter.sortByDate(displayableResultList)

            }
        }
        setUpRecycler(displayableResultList)
    }

// ----------------------------------------- item selected ----------------------------------------------------------------

    private lateinit var selectedResult : UserTestResult
    private lateinit var detailsDialog : Dialog
    private lateinit var favouriteButton : ImageButton
    private lateinit var deleteButton : ImageButton
    override fun itemAtPositionSelected(position: Int) {
        showDetailsDialog(displayableResultList[position])
    }
    override fun itemAtPositionLongClicked(position: Int) {}

    private fun showDetailsDialog(userTestResult: UserTestResult){

        selectedResult = userTestResult
        detailsDialog = Dialog(parentContext)
        detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        detailsDialog.setContentView(R.layout.dialog_test_result_details)
        detailsDialog.findViewById<TextView>(R.id.testDateTextView).text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)
        detailsDialog.findViewById<TextView>(R.id.testResultNameTextView).text = userTestResult.bloodTestName

        var text = ""
        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> text = userTestResult.result.toString() + " " + userTestResult.unit
            RESULT_TYPE_POSITIVE_NEGATIVE -> {
                when(userTestResult.result){
                    POSITIVE_RESULT -> text = resources.getString(R.string.positive)
                    NEGATIVE_RESULT -> text = resources.getString(R.string.negative)
                }
            }
            RESULT_TYPE_DESC -> text = ""
        }
        detailsDialog.findViewById<TextView>(R.id.resultValueTextView).text = text
        detailsDialog.findViewById<TextView>(R.id.notesTextView).text = userTestResult.note

        var favouriteIcon = R.drawable.prestar
        when(userTestResult.favorite){
            IS_FAVORITE -> {
                favouriteIcon = R.drawable.star
            }
        }
        favouriteButton = detailsDialog.findViewById(R.id.imageButtonFavourite)
        favouriteButton.setImageResource(favouriteIcon)
        favouriteButton.setOnClickListener{
            if (selectedResult.favorite == IS_FAVORITE) removeFromFavourites() else addToFavourites()
        }

        deleteButton = detailsDialog.findViewById(R.id.imageButtonDelete)
        deleteButton.setOnClickListener {
            deleteResult()
        }
        detailsDialog.show()
    }

    private fun addToFavourites(){
        selectedResult.favorite = IS_FAVORITE
        favouriteButton.setImageResource(R.drawable.star)
        TestResultsInteractor(UserDataDBEntry).setFavourite(selectedResult, this)
    }

    private fun removeFromFavourites(){
        selectedResult.favorite = NOT_FAVORITE
        favouriteButton.setImageResource(R.drawable.prestar)
        TestResultsInteractor(UserDataDBEntry).setUNFavourite(selectedResult, this)
    }

    private fun deleteResult() {
        TestResultsInteractor(UserDataDBEntry).deleteUserTestResult(selectedResult,this)
        detailsDialog.hide()
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
