package com.example.labmanager.DataBase.DataBaseEntry

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
        if(userTestsResultsArray.size > 0){
            callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            return
        }

        userId = firebaseAuth.currentUser!!.uid
        var userEndpoint = databaseReference.child(USERS_NODE).child(
            userId
        )
        userEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    userTestsResultsArray.add(snapshot.getValue(UserTestResult::class.java) as UserTestResult)
                }
                callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onTestResultRetrievalFailure(DATA_BASE_ERROR)
            }
        })
    }

}