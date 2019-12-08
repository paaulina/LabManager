package com.example.labmanager.DataBase.usecase.UserData.TestResults

import com.example.labmanager.Model.UserTestResult

interface UserTestResultsGetterCallback{
    fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>)
    fun onTestResultRetrievalFailure()
}