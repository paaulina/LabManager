package com.example.labmanager.dataBase.usecase.userData.TestResults

import com.example.labmanager.model.UserTestResult

interface TestResultsRetrievalCallback{
    fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>)
    fun onTestResultRetrievalFailure()
}