package com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading

import com.example.labmanager.DATA_BASE_ERROR
import com.example.labmanager.Model.UserTestResult

class TestResultInteractor (private val gateway: TestResultGateway) :
    TestResultCallback {

    lateinit var successfulTestResultsRetrieval : (ArrayList<UserTestResult>) -> Unit
    lateinit var failureTestResultsRetrieval : (String) -> Unit

    fun retrieveAllTestResultsForUser(
        success: (ArrayList<UserTestResult>) -> Unit,
        failure: (String) -> Unit
    ) {
        successfulTestResultsRetrieval = success
        failureTestResultsRetrieval = failure
        gateway.retrieveAllTestResultsForUser(this)
    }

    override fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>) {
        successfulTestResultsRetrieval(testResults)
    }

    override fun onTestResultRetrievalFailure(message: String?) {
        failureTestResultsRetrieval(DATA_BASE_ERROR)
    }

}