package com.example.labmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import kotlinx.android.synthetic.main.activity_add_test_result.*
import android.text.TextWatcher
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.View.*
import android.view.Window
import android.widget.*
import com.example.labmanager.*
import com.example.labmanager.adapter.BloodTestAdapter
import com.example.labmanager.model.BloodTest
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.DateManager
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import com.example.labmanager.dataBase.dataBaseEntry.StaticDataDBEntry
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.globalData.GlobalDataInteractor
import com.example.labmanager.dataBase.usecase.staticData.StaticDataInteractor
import com.example.labmanager.dataBase.usecase.staticData.StaticDataPresenter
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserGlobalPermissionPresenter
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserProfileDataInteractor
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestsResultSavingPresenter
import com.example.labmanager.service.InternetConnectionChecker


class AddTestResultActivity : AppCompatActivity(),
                              DatePickerDialog.OnDateSetListener,
                              StaticDataPresenter,
                              TestsResultSavingPresenter, UserGlobalPermissionPresenter{

    private lateinit var bloodTestsArray: ArrayList<BloodTest>
    private lateinit var bloodTestAdapter: BloodTestAdapter
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var selectedBloodTest : BloodTest
    private lateinit var unitDialog: Dialog
    private lateinit var firstUnit: String
    private lateinit var secondUnit: String
    private lateinit var typesArray: Array<String>
    private var selectedDateString = ""
    private var selectedResultType = 0
    private var selectedResult = 0f
    private var selectedUnit = ""
    private var insertedNote = ""
    private var POS_NEG_HASHMAP = hashMapOf<String, Float>()
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_test_result)
        setUpTodaysDate()
        setUpPositiveNegativeSpinner()
        val context = this

//        autoCompleteTextView.setOnTouchListener { v, event ->
//            Log.d("AutoComPleTeLog", "sending request")
//            StaticDataInteractor(StaticDataDBEntry, context).getBloodTestsArray()
//            autoCompleteTextView.setOnTouchListener(null)
//            true
//        }
//        Log.d("AutoComPleTeLog", "on create")
//        StaticDataInteractor(StaticDataDBEntry, context).getBloodTestsArray()
        POS_NEG_HASHMAP = hashMapOf(resources.getString(R.string.positive) to 1f, resources.getString(R.string.negative) to 0f)
        typesArray = resources.getStringArray(R.array.result_types_array)

        setUpResultTypeSpinner()
        selectResultType(0)
        unitsTextField.setOnClickListener { showUnitsDialog() }

        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
    }

    override fun onResume() {
        super.onResume()
        Log.d("AutoComPleTeLog", "RESUME")
        insideLayout.visibility = INVISIBLE
        progress_bar_page_loading.visibility = VISIBLE
        StaticDataInteractor(StaticDataDBEntry, this).getBloodTestsArray()
    }

// ----------------------------- DATE -------------------------------------------------------------

    private fun setUpTodaysDate(){

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        showDate(year, month+1, day)
        datePickerDialog = DatePickerDialog(
            this, this, year, month, day
        )
    }
    override fun onDateSet(view: DatePicker?, year0: Int, month0: Int, dayOfMonth0: Int) {
        showDate(year0, month0+1, dayOfMonth0)
    }

    fun setDate(view: View) {
        datePickerDialog.show()
    }

    private fun showDate(year: Int, month: Int, day: Int) {
        var dayString = "$day"
        var monthString = "$month"

        if(day < 10){
            dayString = "0$day"
        }
        if(month < 10){
            monthString = "0$month"
        }
        var dateString = "$dayString/$monthString/$year"
        editTextDate.setText(dateString)
        selectedDateString = dateString
    }

// -------------------BLOOD TEST--------------------------------------------------------------------------

    override fun presentBloodTestsArray(bloodTests: ArrayList<BloodTest>) {
        Log.d("AutoComPleTeLog", "receiving array")
        bloodTestsArray = bloodTests
        setUpAutoCompleteTextViewData()
        progress_bar_page_loading.visibility = GONE
        insideLayout.visibility =  VISIBLE
    }

    override fun presentBloodTestsRetrievalError(error: String) {}

    fun resultsPublished(){
        progress_bar.visibility = INVISIBLE
    }

    private fun setUpAutoCompleteTextViewData(){
        Log.d("AutoComPleTeLog", "settingUpAutocomplete")
        bloodTestAdapter = BloodTestAdapter(
            this,
            R.layout.blood_test_row,
            bloodTestsArray
        )
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.setAdapter(bloodTestAdapter)

        Log.d("AutoComPleTeLog", "settingUpAutocomplete DONE")
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, l ->

            val selectedRow = parent.getItemAtPosition(i)
            selectedBloodTest = selectedRow as BloodTest
            selectResultType(selectedBloodTest.type)
            progress_bar.visibility = INVISIBLE

        }

        autoCompleteTextView.onFocusChangeListener =
            OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    progress_bar.visibility = INVISIBLE
                }
            }
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {0}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("AutoComPleTeLog", "on text changed")
                if (s.isNotEmpty()) {
                    progress_bar.visibility = VISIBLE
                } else {
                    progress_bar.visibility = INVISIBLE
                }
            }
        })


    }

//-----------------------RESULT TYPE -----------------------------------------------------------------------

    private fun setUpResultTypeSpinner(){
        typesArray = resources.getStringArray(R.array.result_types_array)
        val typesSpinner = findViewById<Spinner>(R.id.spinner_test_type)
        val typesAdapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_item,
            typesArray
        )

        typesSpinner.adapter = typesAdapter
        selectResultType(0)

        typesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                selectResultType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectResultType(0)
            }
        }
    }

    fun selectResultType(typeId : Int){

        try {
            selectedBloodTest.type = typeId
        } catch (e : Exception){}
        selectedResultType = typeId
        val typesSpinner = findViewById<Spinner>(R.id.spinner_test_type)
        if(typeId < 0 || typeId >= typesArray.size){
            typesSpinner.setSelection(0)
            setUpLayoutForResultType(typesArray[0])
        }else {
            typesSpinner.setSelection(typeId)
            setUpLayoutForResultType(typesArray[typeId])
        }
    }

    private fun setUpLayoutForResultType(type: String){

        if (type == typesArray[0]){
            numericResultLayout.visibility = VISIBLE
            yesNoResultLayout.visibility = GONE
        } else if (type == typesArray[1]){
            numericResultLayout.visibility = GONE
            yesNoResultLayout.visibility = VISIBLE
        } else if (type == typesArray[2]){
            numericResultLayout.visibility = GONE
            yesNoResultLayout.visibility = GONE
        }
    }

//---------------------- POSITIVE / NEGATIVE -------------------------------------------------------



    private fun getPosNegArray() : ArrayList<String>{
        var posNegArray = arrayListOf<String>()
        POS_NEG_HASHMAP = hashMapOf<String, Float>(resources.getString(R.string.positive) to 1f,
            resources.getString(R.string.negative) to 0f)
        for(values in POS_NEG_HASHMAP.keys){
            posNegArray.add(values)
        }
        return posNegArray
    }

    fun setUpPositiveNegativeSpinner(){
        var posNegArray = getPosNegArray()
        val posNegSpinner = findViewById(R.id.spinner_positive_negative) as Spinner
        val posNegAdapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_item,
            posNegArray
        )

        posNegSpinner.adapter = posNegAdapter

        posNegSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                selectedResult = POS_NEG_HASHMAP.get(posNegArray.get(position))!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }




    private fun showUnitsDialog() {

        unitDialog = Dialog(this)
        unitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        unitDialog.setCancelable(false)
        unitDialog.setTitle("Wybierz jednostkę")
        unitDialog.setContentView(R.layout.dialog_unit_picker)
        val firstUnitSpinner = unitDialog.findViewById(R.id.first_unit_spinner) as Spinner
        val secondUnitSpinner = unitDialog.findViewById(R.id.second_unit_spinner) as Spinner
        val firstAdapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_item,
            FIRST_UNIT_ARRAY
        )
        val secondAdapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_item,
            SECOND_UNIT_ARRAY
        )

        firstUnitSpinner.adapter = firstAdapter
        secondUnitSpinner.adapter = secondAdapter

        firstUnitSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                firstUnit = firstAdapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        secondUnitSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                secondUnit = secondAdapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        unitDialog.show()

    }

    fun setUpUnits(view: View){
        if(firstUnit.isNotEmpty() && secondUnit.isNotEmpty()){
            unitDialog.hide()
            selectedUnit = "$firstUnit/$secondUnit"
            unitsTextField.text = "$firstUnit/$secondUnit"
        }
    }


// ------------------------ SAVE INSERTED DATE ---------------------------------------------------

    var savedResult  = UserTestResult()
    fun addMedicalFile(view: View){
        if(fieldsCorrectlyFilled()
            && InternetConnectionChecker.checkConnection(this, this,
                resources.getString(R.string.set_up_connection_to_continue)
            )){

            val testResult = UserTestResult(
                DateManager.toMillis(editTextDate.text.toString()),
                selectedResult,
                insertedNote,
                selectedUnit,
                0,
                selectedResultType,
                //selectedBloodTest.name!!
                autoCompleteTextView.text.toString()


            )
            savedResult = testResult
            TestResultsInteractor(
                UserDataDBEntry
            ).saveUserTestResult(testResult, this)

        }
    }

    fun fieldsCorrectlyFilled(): Boolean{
        if(autoCompleteTextView.text.isEmpty()){
            return false
        }
        if(!::selectedBloodTest.isInitialized && autoCompleteTextView.text.length == 0){
            return false
        }
        if(selectedResultType == 0 ){
            if(editTextReslt.text.length == 0){
                return false
            }
            selectedResult = editTextReslt.text.toString().toFloat()
            if(!::firstUnit.isInitialized || !::secondUnit.isInitialized){
                return false
            }
        }

        if(selectedResultType == 2 && editTextNotes.text.length == 0){
            return false
        }

        if(editTextNotes.text.length > 0 ){
            insertedNote = editTextNotes.text.toString()
        }

        return true;
    }

    override fun presentSaveSuccess() {
        UserProfileDataInteractor(UserDataDBEntry).getGlobalPermission(this)

    }

    override fun presentSaveError() {}

    override fun presentGlobalPersmission(permission: Int) {
        if(permission == GLOBAL_ALLOWED){
            GlobalDataInteractor.save(savedResult)
        }
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra(SUCCESS_ACTIVITY_ENTRY_POINT, TEST_RESULT_ADITION)
        startActivity(intent)
    }

    override fun presentPermissionRetrievalFailure() {
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra(SUCCESS_ACTIVITY_ENTRY_POINT, TEST_RESULT_ADITION)
        startActivity(intent)
    }




}
