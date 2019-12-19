package com.example.labmanager.DataBase.usecase.UserData.TestResults

import com.example.labmanager.Model.UserTestResult

interface TestResultsPresenter{
    fun presentUsersTestResults(testResults: ArrayList<UserTestResult>)
    fun presentRetrievalError()
}