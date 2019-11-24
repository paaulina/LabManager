package com.example.labmanager.DataBase.DataBaseEntry

import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.DataBase.usecase.UserData.UserTestResultsSaving.UserTestResultSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.UserTestResultsSaving.UserTestResutSavingGatewey
import com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading.TestResultCallback
import com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading.TestResultGateway
import com.example.labmanager.Model.BloodTest
import com.example.labmanager.Model.UserTestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object UserDataDBEntry : UserTestResutSavingGatewey, TestResultGateway{

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""
    private var userTestsResultsArray = arrayListOf<UserTestResult>()
    private var uploaded = false

    override fun saveUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback) {
        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )

            var key = userEndpoint.child(TESTS_RESULTS_NODE).push().key
            if (key != null) {
                writeTestResult(userEndpoint.child(TESTS_RESULTS_NODE).child(key), userTestResult)
                userTestsResultsArray.add(userTestResult)
                callback.onSuccess()
                return
            }
        }
        callback.onFailure()
    }

    private fun writeTestResult(databaseReference: DatabaseReference, userTestResult: UserTestResult){
        databaseReference.child(DATE).setValue(userTestResult.dateMillis)
        databaseReference.child(BLOODTEST_NAME).setValue(userTestResult.bloodTestName)
        databaseReference.child(RESULT_TYPE).setValue(userTestResult.resultType)
        databaseReference.child(RESULT).setValue(userTestResult.result)
        databaseReference.child(UNIT).setValue(userTestResult.unit)
        databaseReference.child(NOTES).setValue(userTestResult.note)
    }

    override fun retrieveAllTestResultsForUser(callback: TestResultCallback) {
        if(userTestsResultsArray.size > 0 && uploaded){
            callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            return
        }

        userId = firebaseAuth.currentUser!!.uid
        var userEndpoint = databaseReference.child(USERS_NODE).child(
            userId
        ).child(TESTS_RESULTS_NODE)

        Log.d("userdblog " , "userdblog " + userEndpoint)
        userEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){





                    userTestsResultsArray.add(userTestResultFromDataSnapshot(snapshot))
                    //userTestsResultsArray.add(snapshot.getValue(UserTestResult::class.java) as UserTestResult)
                    //Log.d("userdblog " , "userdblog name " + (snapshot.getValue(UserTestResult::class.java) as UserTestResult).bloodTestName)
                }
                uploaded = true
                callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onTestResultRetrievalFailure(DATA_BASE_ERROR)
            }
        })
    }


    fun userTestResultFromDataSnapshot(dataSnapshot: DataSnapshot) : UserTestResult{
        var userTestResult = UserTestResult()

        for(snap in dataSnapshot.children){
            var key = snap.key

            when(snap.key){
                DATE -> userTestResult.dateMillis = snap.getValue(Long::class.java)!!
                FAVORITE -> userTestResult.favorite = snap.getValue(Int::class.java)!!
                BLOODTEST_NAME -> userTestResult.bloodTestName = snap.getValue(String::class.java)!!
                NOTES -> userTestResult.note = snap.getValue(String::class.java)!!
                RESULT_TYPE -> userTestResult.resultType = snap.getValue(Int::class.java)!!
                UNIT -> userTestResult.unit = snap.getValue(String::class.java)!!
                RESULT -> userTestResult.result = snap.getValue(Float::class.java)!!
            }
            Log.d("child1", "child1 key  " + key + "  val " + snap.getValue(Any::class.java))
        }
        return userTestResult
    }



}