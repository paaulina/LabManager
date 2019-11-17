package com.example.labmanager.Activity

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
import android.view.MotionEvent
import android.view.Window
import android.widget.*
import com.example.labmanager.*
import com.example.labmanager.Adapter.BloodTestAdapter
import com.example.labmanager.DataBase.DataBaseManager
import com.example.labmanager.DataBase.usecase.BloodTestsInteractor
import com.example.labmanager.DataBase.usecase.BloodTestsPresenter
import com.example.labmanager.Model.BloodTest
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DateManager
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import android.view.View.OnFocusChangeListener
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class AddTestResultActivity : AppCompatActivity(), BloodTestsPresenter {

    lateinit var bloodTestsArray: ArrayList<BloodTest>
    lateinit var bloodTestAdapter: BloodTestAdapter
    var year = 0
    var month = 0
    var day = 0
    lateinit var selectedBloodTest : BloodTest
    lateinit var unitDialog: Dialog
    lateinit var firstUnit: String
    lateinit var secondUnit: String
    lateinit var typesArray: Array<String>
    var selectedDateString = ""
    var selectedResultType = 0;
    var selectedResult = 0f
    var selectedUnit = ""
    var insertedNote = ""
    var POS_NEG_HASHMAP = hashMapOf<String, Float>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_test_result)
        setUpTodaysDate()
        setUpPositiveNegativeSpinner()
        var context = this
        autoCompleteTextView.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //progress_bar.visibility = View.VISIBLE
                var retrieval = BloodTestsInteractor(DataBaseManager, context)
                retrieval.dispatch()

                autoCompleteTextView.setOnTouchListener(null)
                return true
            }

        })
        autoCompleteTextView.setOnClickListener {
            //progress_bar.visibility = View.VISIBLE
            var retrieval = BloodTestsInteractor(DataBaseManager, this)
            retrieval.dispatch()
        }

        POS_NEG_HASHMAP = hashMapOf<String, Float>(resources.getString(R.string.positive) to 1f,
            resources.getString(R.string.negative) to 0f)

        typesArray = resources.getStringArray(R.array.result_types_array)
        setUpResultTypeSpinner()
        selectResultType(0)
        unitsTextField.setOnClickListener { showUnitsDialog() }
    }

// ----------------------------- DATE -------------------------------------------------------------

    fun setUpTodaysDate(){
        var calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day)
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
        selectedDateString = dateString
    }

// -------------------BLOOD TEST--------------------------------------------------------------------------

    override fun presentBloodTests(bloodTests: ArrayList<BloodTest>) {
        bloodTestsArray = bloodTests
        setUpAutoCompleteTextViewData()
    }

    override fun presentBloodTestsError(message: String?) {
        Toast.makeText(this,"Error", Toast.LENGTH_LONG)
    }

    fun setUpAutoCompleteTextViewData(){
        bloodTestAdapter = BloodTestAdapter(
            this,
            R.layout.blood_test_row,
            bloodTestsArray
        )
        autoCompleteTextView.threshold = 0
        autoCompleteTextView.setAdapter(bloodTestAdapter)
        //progress_bar.visibility = View.INVISIBLE


        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, l ->

            var selectedRow = parent.getItemAtPosition(i)
            selectedBloodTest = selectedRow as BloodTest
            Log.d("SelectedBloodTest " , "SelectedBloodTest id:${selectedBloodTest.name} name:${selectedBloodTest.name} type:${selectedBloodTest.type}" )
            selectResultType(selectedBloodTest.type)
            progress_bar .visibility = View.INVISIBLE

        }

        autoCompleteTextView.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    progress_bar.visibility = View.INVISIBLE
                }
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

//-----------------------RESULT TYPE -----------------------------------------------------------------------

    fun setUpResultTypeSpinner(){
        typesArray = resources.getStringArray(R.array.result_types_array)
        val typesSpinner = findViewById(R.id.spinner_test_type) as Spinner
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
        val typesSpinner = findViewById(R.id.spinner_test_type) as Spinner
        if(typeId < 0 || typeId >= typesArray.size){
            typesSpinner.setSelection(0)
            setUpLayoutForResultType(typesArray.get(0))
        }else {
            typesSpinner.setSelection(typeId)
            setUpLayoutForResultType(typesArray.get(typeId))
        }
    }

    fun setUpLayoutForResultType(type: String){

        if (type.equals(typesArray.get(0))){
            numericResultLayout.visibility = View.VISIBLE
            yesNoResultLayout.visibility = View.GONE
        } else if (type.equals(typesArray.get(1))){
            numericResultLayout.visibility = View.GONE
            yesNoResultLayout.visibility = View.VISIBLE
        } else if (type.equals(typesArray.get(2))){
            numericResultLayout.visibility = View.GONE
            yesNoResultLayout.visibility = View.GONE
        }
    }

//---------------------- POSITIVE / NEGATIVE -------------------------------------------------------



    fun getPosNegArray() : ArrayList<String>{
        var posNegArray = arrayListOf<String>()
        POS_NEG_HASHMAP = hashMapOf<String, Float>(resources.getString(R.string.positive) to 1f,
            resources.getString(R.string.negative) to 0f)
        for(vals in POS_NEG_HASHMAP.keys){
            posNegArray.add(vals)
            Log.d("PosNegLog " , "PosNegvals $vals")
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
        finish()
    }
    override fun onBackPressed(){}




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

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO
            }
        }

        secondUnitSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                secondUnit = secondAdapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO
            }
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


    fun addMedicalFile(view: View){
        if(fieldsCorrectlyFilled()){

            var testResult = UserTestResult(
                DateManager.toMillis(editTextDate.text.toString()),
                selectedBloodTest.name!!,
                selectedResultType,
                selectedResult,
                selectedUnit,
                insertedNote
            )

            if(DataBaseManager.saveUserTestResult(testResult)){
                val intent = Intent(this, SuccessActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Saving Failed", Toast.LENGTH_LONG)
            }
        }
    }

    fun fieldsCorrectlyFilled(): Boolean{
        if(selectedBloodTest == null && autoCompleteTextView.text.length == 0){
            return false
        }
        if(selectedResultType == 0 ){
            if(editTextReslt.text.length == 0){
                return false
            }
            selectedResult = editTextReslt.text.toString().toFloat()
            if(firstUnit == null || secondUnit == null){
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




}
