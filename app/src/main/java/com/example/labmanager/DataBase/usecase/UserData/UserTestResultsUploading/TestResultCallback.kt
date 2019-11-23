package com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading

import com.example.labmanager.Model.UserTestResult

interface TestResultCallback{
    fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>)
    fun onTestResultRetrievalFailure(message: String?)
}