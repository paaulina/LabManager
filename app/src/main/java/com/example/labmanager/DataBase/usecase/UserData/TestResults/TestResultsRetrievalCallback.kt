package com.example.labmanager.DataBase.usecase.UserData.TestResults

import com.example.labmanager.Model.UserTestResult

interface TestResultsRetrievalCallback{
    fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>)
    fun onTestResultRetrievalFailure()
}