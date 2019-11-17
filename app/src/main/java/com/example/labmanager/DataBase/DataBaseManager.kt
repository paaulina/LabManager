package com.example.labmanager.DataBase

import android.util.Log
import com.example.labmanager.DataBase.usecase.BloodTestsCallback
import com.example.labmanager.DataBase.usecase.BloodTestsGateway
import com.example.labmanager.Model.BloodTest
import com.example.labmanager.Model.UserTestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*
    Database use cases:
    1. Reading data:
        - get all blood tests names: for adding new blood test result
        - display user's blood test results
        - display group of blood test results
        - display chart for blood tests
 */



object DataBaseManager : BloodTestsGateway {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var bloodTestsArray = ArrayList<BloodTest>()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""


    fun initBloodTestsArray(){
        if(bloodTestsArray.size == 0){
            var btEndpoint = databaseReference.child("BloodTests")
            btEndpoint.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for(btSnapshot in dataSnapshot.children){
                        bloodTestsArray.add(btSnapshot.getValue(BloodTest::class.java) as BloodTest)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Debugging ", databaseError.message)
                }
            })
        }
    }

    override fun retrieveBloodTests(callback: BloodTestsCallback) {
        var btEndpoint = databaseReference.child("BloodTests")
        btEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(btSnapshot in dataSnapshot.children){
                    bloodTestsArray.add(btSnapshot.getValue(BloodTest::class.java) as BloodTest)
                }
                callback.onBloodTestsRetrievalSuccess(bloodTestsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onBloodTestsRetrievalFailure("Error")
            }
        })
    }

    fun getUserNode(){
        if(userId.isEmpty()){
            if(firebaseAuth.currentUser != null){
                userId = firebaseAuth.currentUser!!.uid
                var userEndpoint = databaseReference.child("Users").child(
                    userId
                )

                //var key = userEndpoint.child("TestResults").push().key
                //userEndpoint.child("TestResults").child(key!!).setValue("test45")
                userEndpoint.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.value == null){

                            //userEndpoint.child("Groups").setValue("123")
                            Log.d("savingDB5" , "savingDB5")
                            //databaseReference.child("BloodTests").child("01"). setValue("temp")

                        }

                    }
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }


                })
            }
        }

    }

    val USERS_NODE = "Users"
    val TESTS_RESULTS_NODE = "TestResults"
    val DATE = "dt"
    //val BLOODTEST_ID = "id"
    val BLOODTEST_NAME = "nm"
    val RESULT_TYPE = "tp"
    val RESULT = "val"
    val UNIT = "unit"
    val NOTES = "note"


    fun saveUserTestResult(userTestResult: UserTestResult) : Boolean{
        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )

            var key = userEndpoint.child(TESTS_RESULTS_NODE).push().key
            if (key != null) {
                writeTestResult(userEndpoint.child(TESTS_RESULTS_NODE).child(key), userTestResult)
                return true
            }
        }
        return false
    }

    private fun writeTestResult(databaseReference: DatabaseReference, userTestResult: UserTestResult){
        databaseReference.child(DATE).setValue(userTestResult.dateMillis)
        //databaseReference.child(BLOODTEST_ID).setValue(userTestResult.bloodTestId)
        databaseReference.child(BLOODTEST_NAME).setValue(userTestResult.bloodTestName)
        databaseReference.child(RESULT_TYPE).setValue(userTestResult.resultType)
        databaseReference.child(RESULT).setValue(userTestResult.result)
        databaseReference.child(UNIT).setValue(userTestResult.unit)
        databaseReference.child(NOTES).setValue(userTestResult.note)
    }

    fun getFirstUnitsArray() : ArrayList<String>{
        return arrayListOf<String>("")

    }

}