package com.example.labmanager.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.*
import com.example.labmanager.Adapter.TestResultAdapter
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DateManager
import com.example.labmanager.Service.ItemClickedCallback
import com.example.labmanager.Service.TestResultsFilter
import kotlinx.android.synthetic.main.fragment_tests_results_overview.*
import kotlin.collections.ArrayList
import android.content.res.Resources
import android.view.*
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsPresenter
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestsResultSavingPresenter
import com.example.labmanager.Service.UserTestsResultsGroupManager


class TestsResultsOverviewFragment(
    context: Context
) : Fragment(),
    DatePickerDialog.OnDateSetListener,
    ItemClickedCallback,
    TestResultsPresenter,
    TestResultsSavingCallback,
    TestsResultSavingPresenter{


    var parentContext = context
    lateinit var filtersDialog: Dialog
    lateinit var datePickerDialog: DatePickerDialog
    var stringDateFrom = ""
    var stringDateTo = ""

    var dateDialogEntry = 0
    var ENTRY_DATE_FROM = 1
    var ENTRY_DATE_TO = 2

    var resultsList = arrayListOf<UserTestResult>()
    var displayableResultList = arrayListOf<UserTestResult>()

    var showingOnlyFavs = false

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


    fun showOnlyFavourites(){

        buttonFavFilter.setImageDrawable(resources.getDrawable(R.drawable.star))
        var favitems = arrayListOf<UserTestResult>()
        for(res in displayableResultList){
            if(res.favorite == IS_FAVORITE){
                favitems.add(res)
            }
        }
        displayableResultList = favitems
        setUpRecycler(displayableResultList)
    }


    fun setUpRecycler(results: ArrayList<UserTestResult>){
        displayableResultList = results
        val recyclerAdapter = TestResultAdapter(displayableResultList, parentContext, this)
        resltsRecyclerOverview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecyclerOverview.setHasFixedSize(true)
        resltsRecyclerOverview.adapter = recyclerAdapter
        //resltsRecyclerOverview.setNestedScrollingEnabled(false)
    }



//################################## FILTERS DIALOG ##################################################

    lateinit var filterNameEditText : EditText
    lateinit var dateFromEditText : EditText
    lateinit var dateToEditText : EditText
    var filterName : String = ""
    var dateFromSet = false
    var dateToSet = false
    lateinit var sortingSpinner : Spinner

    fun showFiltersDialog(){
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

    fun setUpFiltersFields(){
        Log.d("Filtersdoc" , "$filterName $stringDateTo")
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
            ENTRY_DATE_FROM -> showDateFrom(year, month+1, dayOfMonth)
            ENTRY_DATE_TO -> showDateTo(year, month+1, dayOfMonth)
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

        dateFromEditText.setText(dateString)
        dateFromSet = true
        stringDateFrom = dateString

        Log.d("dateFromSetting " , " date drom setting $dateString")
    }

    private fun showDateTo(year: Int, month: Int, day: Int) {
        var dateString = "$day/$month/$year"
        if(day < 10){
            dateString = "0$dateString"
        }

        dateToEditText.setText(dateString)
        dateToSet = true
        stringDateTo = dateString
        Log.d("dateToSetting " , " date to setting $dateString")
    }

    var sortingTypesInitialized = false
    lateinit var sortingTypesArray : Array<String>
    var selectedSorting = ""
    fun setUpSortingSpinner(){

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


    fun applyFilters(){
        buttonFavFilter.setImageDrawable(resources.getDrawable(R.drawable.prestar))
        displayableResultList = resultsList
        if(filterName.length > 0){
            displayableResultList = TestResultsFilter.filterByName(filterName, displayableResultList)
        }

        if(dateFromSet){
            displayableResultList = TestResultsFilter.filterByDateFrom(DateManager.toMillis(stringDateFrom), displayableResultList)
            //stringDateFrom = ""
        }

        if(dateToSet){
            displayableResultList = TestResultsFilter.filterByDateTo(DateManager.toMillis(stringDateTo), displayableResultList)
            //stringDateTo = ""
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

    lateinit var selectedResult : UserTestResult
    lateinit var detailsDialog : Dialog
    lateinit var favouriteButton : ImageButton
    lateinit var deleteButton : ImageButton
    override fun itemAtPositionSelected(position: Int) {
        showDetailsDialog(displayableResultList.get(position))
    }
    override fun itemAtPositionLongClicked(position: Int) {
        Log.d("LongClick1", "longclikkkk")
//        if(position >= 0)
//            UserDataDBEntry.deleteUserTestResult(displayableResultList.get(position), this)
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

    fun showDetailsDialog(userTestResult: UserTestResult){

        selectedResult = userTestResult
        detailsDialog = Dialog(parentContext)
        detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        detailsDialog.setContentView(R.layout.dialog_test_result_details)

        detailsDialog.findViewById<TextView>(R.id.testDateTextView).text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)
        detailsDialog.findViewById<TextView>(R.id.testResultNameTextView).text = userTestResult.bloodTestName

        var text = ""
        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> text = userTestResult.result.toString() + " " + userTestResult.unit.toString()
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
        favouriteButton = detailsDialog.findViewById<ImageButton>(R.id.imageButtonFavourite)
        favouriteButton.setImageResource(favouriteIcon)
        favouriteButton.setOnClickListener{
            if (selectedResult.favorite == IS_FAVORITE) removeFromFavourites() else addToFavourites()
        }

        deleteButton = detailsDialog.findViewById<ImageButton>(R.id.imageButtonDelete)
        deleteButton.setOnClickListener {
            deleteResult(selectedResult.id)
        }
        detailsDialog.show()
    }

    fun addToFavourites(){
        selectedResult.favorite = IS_FAVORITE
        favouriteButton.setImageResource(R.drawable.star)
        //UserDataDBEntry.updateUserTestResult(selectedResult, this)
        TestResultsInteractor(UserDataDBEntry).setFavourite(selectedResult, this)

    }

    fun removeFromFavourites(){
        selectedResult.favorite = NOT_FAVORITE
        favouriteButton.setImageResource(R.drawable.prestar)
        //UserDataDBEntry.updateUserTestResult(selectedResult, this)
        TestResultsInteractor(UserDataDBEntry).setUNFavourite(selectedResult, this)
    }


    fun deleteResult(testId: String){
        TestResultsInteractor(UserDataDBEntry).deleteUserTestResult(selectedResult,this)
        detailsDialog.hide()
    }

    override fun presentSaveSuccess() {
        //detailsDialog.hide()

    }

    override fun presentSaveError() {}

    fun closeDialog(isSuccess: Boolean){

    }

    override fun onSaveSuccess() {
        TestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }

    override fun onSaveFailure() {
    }

}
