package com.example.labmanager.DataBase.usecase.UserData.UserTestResultsSaving

import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.TESTS_RESULTS_NODE
import com.example.labmanager.USERS_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserTestResultSaveInteractor (private val userTestResutSavingGatewey: UserTestResutSavingGatewey) : UserTestResultSavingCallback {

    lateinit var callBackFunction : (Boolean) -> Unit

    fun saveUserTestResult(
        userTestResult: UserTestResult,
        callbackFunction: (Boolean) -> Unit
    ) {

        callBackFunction = callbackFunction
        userTestResutSavingGatewey.saveUserTestResult(userTestResult, this)

    }

    override fun onSuccess() {
        callBackFunction(true)
    }

    override fun onFailure() {
        callBackFunction(false)
    }

}